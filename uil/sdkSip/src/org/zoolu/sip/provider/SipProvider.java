/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * 
 * This file is part of MjSip (http://www.mjsip.org)
 * 
 * MjSip is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * MjSip is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MjSip; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 * Nitin Khanna, Hughes Systique Corp. (Reason: Android specific change,
 * optmization, bug fix)
 */

package org.zoolu.sip.provider;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.zoolu.net.IpAddress;
import org.zoolu.net.SocketAddress;
import org.zoolu.net.TcpServer;
import org.zoolu.net.TcpServerListener;
import org.zoolu.net.TcpSocket;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.header.ViaHeader;
import org.zoolu.sip.message.Message;
import org.zoolu.tools.Configurable;
import org.zoolu.tools.Configure;
import org.zoolu.tools.HashSet;
import org.zoolu.tools.Iterator;
import org.zoolu.tools.Parser;
import org.zoolu.tools.Random;
import org.zoolu.tools.SimpleDigest;

import android.text.TextUtils;

import com.jiaxun.sdk.util.log.Log;

/**
 * SipProvider implements the SIP transport layer, that is the layer responsable
 * for sending and receiving SIP messages. Messages are received by the callback
 * function defined in the interface SipProviderListener.
 * <p>
 * SipProvider implements also multiplexing/demultiplexing service through the
 * use of SIP interface identifiers and <i>onReceivedMessage()<i/> callback
 * function of specific SipProviderListener.
 * <p>
 * A SipProviderListener can be added to a SipProvider through the
 * addSipProviderListener(id,listener) method, where: <b> - <i>id<i/> is the SIP
 * interface identifier the listener has to be bound to, <b> - <i>listener<i/>
 * is the SipProviderListener that received messages are passed to.
 * <p/>
 * The SIP interface identifier specifies the type of messages the listener is
 * going to receive for. Together with the specific SipProvider, it represents
 * the complete SIP Service Access Point (SAP) address/identifier used for
 * demultiplexing SIP messages at receiving side.
 * <p/>
 * The identifier can be of one of the three following types: transaction_id,
 * dialog_id, or method_id. These types of identifiers characterize
 * respectively: <br>
 * - messages within a specific transaction, <br>
 * - messages within a specific dialog, <br>
 * - messages related to a specific SIP method. It is also possible to use the
 * the identifier ANY to specify <br>
 * - all messages that are out of any transactions, dialogs, or already
 * specified method types.
 * <p>
 * When receiving a message, the SipProvider first tries to look for a matching
 * transaction, then looks for a matching dialog, then for a matching method
 * type, and finally for a default listener (i.e. that with identifier ANY). For
 * the matched SipProviderListener, the method <i>onReceivedMessage()</i> is
 * fired.
 * <p>
 * Note: no 482 (Loop Detected) responses are generated for requests that does
 * not properly match any ongoing transactions, dialogs, nor method types.
 */
public class SipProvider implements Configurable, TransportListener, TcpServerListener
{

    // **************************** Constants ****************************

    /** UDP protocol type */
    public static final String PROTO_UDP = "udp";
    /** TCP protocol type */
    public static final String PROTO_TCP = "tcp";
    /** TLS protocol type */
    public static final String PROTO_TLS = "tls";
    /** SCTP protocol type */
    public static final String PROTO_SCTP = "sctp";

    /**
     * String value "auto-configuration" used for auto configuration of the host
     * address.
     */
    public static final String AUTO_CONFIGURATION = "AUTO-CONFIGURATION";

    /**
     * String value "auto-configuration" used for auto configuration of the host
     * address.
     */
    public static final String ALL_INTERFACES = "ALL-INTERFACES";

    /** String value "NO-OUTBOUND" used for setting no outbound proxy. */
    // public static final String NO_OUTBOUND="NO-OUTBOUND";
    /**
     * Identifier used as listener id for capturing ANY incoming messages that
     * does not match any active method_id, transaction_id, nor dialog_id. <br>
     * In this context, "active" means that there is a active listener for that
     * specific method, transaction, or dialog.
     */
    public static final Identifier ANY = new Identifier("ANY");

    /**
     * Identifier used as listener id for capturing any incoming messages in
     * PROMISQUE mode, that means that messages are passed to the present
     * listener regardless of any other active SipProviderListeners for specific
     * messages.
     * <p/>
     * More than one SipProviderListener can be added and be active concurrently
     * for capturing messages in PROMISQUE mode.
     */
    public static final Identifier PROMISQUE = new Identifier("PROMISQUE");

    /** Minimum length for a valid SIP message. */
    private static final int MIN_MESSAGE_LENGTH = 12;

    // ***************** Readable/configurable attributes *****************

    /**
     * Via address/name. Use 'auto-configuration' for auto detection, or let it
     * undefined.
     */
    String via_addr = null;

    /** Local SIP port */
    int host_port = 0;

    /**
     * Network interface (IP address) used by SIP. Use 'ALL-INTERFACES' for
     * binding SIP to all interfaces (or let it undefined).
     */
    String host_ifaddr = null;

    /** Transport protocols (the first protocol is used as default) */
    String[] transport_protocols = null;

    /** Max number of (contemporary) open connections */
    int nmax_connections = 0;

    /**
     * Outbound proxy (host_addr[:host_port]). Use 'NONE' for not using an
     * outbound proxy (or let it undefined).
     */
    SocketAddress outbound_proxy = null;

    /** Whether logging all packets (including non-SIP keepalive tokens). */
    boolean log_all_packets = false;

    // for backward compatibility:

    /** Outbound proxy addr (for backward compatibility). */
    private String outbound_addr = null;
    /** Outbound proxy port (for backward compatibility). */
    private int outbound_port = -1;

    // ********************* Non-readable attributes *********************

    /** Network interface (IP address) used by SIP. */
    IpAddress host_ipaddr = null;

    /** Default transport */
    String default_transport = null;

    /** Whether using UDP as transport protocol */
    boolean transport_udp = false;
    /** Whether using TCP as transport protocol */
    boolean transport_tcp = false;
    /** Whether using TLS as transport protocol */
    boolean transport_tls = false;
    /** Whether using SCTP as transport protocol */
    boolean transport_sctp = false;

    /** Whether adding 'rport' parameter on outgoing requests. */
    boolean rport = true;

    /**
     * Whether forcing 'rport' parameter on incoming requests ('force-rport'
     * mode).
     */
    boolean force_rport = false;

    /** List of provider listeners */
    Hashtable<Identifier, SipProviderListener> listeners = null;

    /** List of exception listeners */
    HashSet exception_listeners = null;

    /** UDP transport */
    UdpTransport udp = null;

    /** Tcp server */
    TcpServer tcp_server = null;

    /** Connections */
    Hashtable<ConnectionIdentifier, ConnectedTransport> connections = null;

    private String destAddr;// 服务器地址
    
    private int destPort;// 服务器端口
    
    private String origAddr;// 服务器uri

    private static String TAG = "SipProvider";

    /** Creates a new SipProvider. */
    public SipProvider(String via_addr, int port)
    {
        init(via_addr, port, null, null);
        startTrasport();
    }

    /**
     * Creates a new SipProvider. Costructs the SipProvider, initializing the
     * SipProviderListeners, the transport protocols, and other attributes.
     */
    public SipProvider(String via_addr, int port, String[] protocols, String ifaddr)
    {
        init(via_addr, port, protocols, ifaddr);
        startTrasport();
    }

    /**
     * Creates a new SipProvider. The SipProvider attributres are read from
     * file.
     */
    public SipProvider(String file)
    {
        if (!SipStack.isInit())
            SipStack.init(file);
        new Configure(this, file);
        init(via_addr, host_port, transport_protocols, host_ifaddr);
        startTrasport();
    }

    /**
     * Inits the SipProvider, initializing the SipProviderListeners, the
     * transport protocols, the outbound proxy, and other attributes.
     */
    private void init(String viaddr, int port, String[] protocols, String ifaddr)
    {
        if (!SipStack.isInit())
            SipStack.init();
        via_addr = viaddr;
        if (via_addr == null || via_addr.equalsIgnoreCase(AUTO_CONFIGURATION))
            via_addr = IpAddress.localIpAddress;
        host_port = port;
        if (host_port < 0) // modified
            host_port = SipStack.default_port;
        host_ipaddr = null;
        if (ifaddr != null && !ifaddr.equalsIgnoreCase(ALL_INTERFACES))
        {
            try
            {
                host_ipaddr = IpAddress.getByName(ifaddr);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                host_ipaddr = null;
            }
        }
        transport_protocols = protocols;
        if (transport_protocols == null)
            transport_protocols = SipStack.default_transport_protocols;
        default_transport = transport_protocols[0];
        for (int i = 0; i < transport_protocols.length; i++)
        {
            transport_protocols[i] = transport_protocols[i].toLowerCase();
            if (transport_protocols[i].equals(PROTO_UDP))
                transport_udp = true;
            else if (transport_protocols[i].equals(PROTO_TCP))
                transport_tcp = true;
            /*
             * else if (transport_protocols[i].equals(PROTO_TLS))
             * transport_tls=true; else if
             * (transport_protocols[i].equals(PROTO_SCTP)) transport_sctp=true;
             */
        }
        if (nmax_connections <= 0)
            nmax_connections = SipStack.default_nmax_connections;

        // just for backward compatibility..
        if (outbound_port < 0)
            outbound_port = SipStack.default_port;
        if (outbound_addr != null)
        {
            if (outbound_addr.equalsIgnoreCase(Configure.NONE) || outbound_addr.equalsIgnoreCase("NO-OUTBOUND"))
                outbound_proxy = null;
            else
                outbound_proxy = new SocketAddress(outbound_addr, outbound_port);
        }

        rport = SipStack.use_rport;
        force_rport = SipStack.force_rport;

        exception_listeners = new HashSet();
        listeners = new Hashtable<Identifier, SipProviderListener>(10);

        connections = new Hashtable<ConnectionIdentifier, ConnectedTransport>(10);
    }

    /** Starts the transport services. */
    private void startTrasport()
    {
        int host_port_max = host_port + 100;
        // start udp
        if (transport_udp)
        {
            for (int i = host_port; i < host_port_max; i++)
            {
                try
                {
                    if (host_ipaddr == null)
                        udp = new UdpTransport(i, this);
                    else
                        udp = new UdpTransport(i, host_ipaddr, this);
                    host_port = udp.getPort();
                    Log.info(TAG, "startTrasport::udp is up");
                    break;// 退出
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
            }
        }
        // start tcp
        if (transport_tcp)
        {
            for (int i = host_port; i < host_port_max; i++)
            {
                try
                {
                    if (host_ipaddr == null)
                        tcp_server = new TcpServer(host_port, this);
                    else
                        tcp_server = new TcpServer(host_port, host_ipaddr, this);
                    host_port = tcp_server.getPort();
                    Log.info(TAG, "startTrasport::tcp is up");
                    break;// 退出
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
            }
        }
    }

    /** Stops the transport services. */
    private void stopTrasport()
    {
        // stop udp
        if (udp != null)
        {
            Log.error(TAG, "stopTrasport::udp is going down");
            udp.halt();
            udp = null;
        }
        // stop tcp
        if (tcp_server != null)
        {
            Log.error(TAG, "stopTrasport::tcp is going down");
            tcp_server.halt();
            tcp_server = null;
        }
        haltConnections();
        connections = null;
    }

    public void haltConnections()
    { // modified
        if (connections != null)
        {
            Log.error(TAG, "haltConnections::connections are going down");
            for (Enumeration<ConnectedTransport> e = connections.elements(); e.hasMoreElements();)
            {
                ConnectedTransport c = e.nextElement();
                c.halt();
            }
            connections = new Hashtable<ConnectionIdentifier, ConnectedTransport>(10);
        }
    }

    /** Stops the SipProviders. */
    public void halt()
    {
        Log.error(TAG, "halt: SipProvider is going down");
        haltConnections();// 停止sip重传
        if (listeners != null)
        {
            listeners.clear();
        }
        else
        {
            listeners = new Hashtable<Identifier, SipProviderListener>(10);
        }

        if (exception_listeners != null)
        {
            exception_listeners.clear();
        }
        else
        {
            exception_listeners = new HashSet();
        }
        exception_listeners = new HashSet();
    }

    /** Parses a single line (loaded from the config file) */
    public void parseLine(String line)
    {
        String attribute;
        Parser par;
        int index = line.indexOf("=");
        if (index > 0)
        {
            attribute = line.substring(0, index).trim();
            par = new Parser(line, index + 1);
        }
        else
        {
            attribute = line;
            par = new Parser("");
        }
        char[] delim = { ' ', ',' };

        if (attribute.equals("via_addr"))
        {
            via_addr = par.getString();
            return;
        }
        if (attribute.equals("host_port"))
        {
            host_port = par.getInt();
            return;
        }
        if (attribute.equals("host_ifaddr"))
        {
            host_ifaddr = par.getString();
            return;
        }
        if (attribute.equals("transport_protocols"))
        {
            transport_protocols = par.getWordArray(delim);
            return;
        }
        if (attribute.equals("nmax_connections"))
        {
            nmax_connections = par.getInt();
            return;
        }
        if (attribute.equals("outbound_proxy"))
        {
            String soaddr = par.getString();
            if (soaddr == null || soaddr.length() == 0 || soaddr.equalsIgnoreCase(Configure.NONE) || soaddr.equalsIgnoreCase("NO-OUTBOUND"))
                outbound_proxy = null;
            else
                outbound_proxy = new SocketAddress(soaddr);
            return;
        }
        if (attribute.equals("log_all_packets"))
        {
            log_all_packets = (par.getString().toLowerCase().startsWith("y"));
            return;
        }

        // old parameters
        if (attribute.equals("host_addr"))
            System.err.println("WARNING: parameter 'host_addr' is no more supported; use 'via_addr' instead.");
        if (attribute.equals("all_interfaces"))
            System.err
                    .println("WARNING: parameter 'all_interfaces' is no more supported; use 'host_iaddr' for setting a specific interface or let it undefined.");
        if (attribute.equals("use_outbound"))
            System.err
                    .println("WARNING: parameter 'use_outbound' is no more supported; use 'outbound_proxy' for setting an outbound proxy or let it undefined.");
        if (attribute.equals("outbound_addr"))
        {
            System.err.println("WARNING: parameter 'outbound_addr' has been deprecated; use 'outbound_proxy=<host_addr>[:<host_port>]' instead.");
            outbound_addr = par.getString();
            return;
        }
        if (attribute.equals("outbound_port"))
        {
            System.err.println("WARNING: parameter 'outbound_port' has been deprecated; use 'outbound_proxy=<host_addr>[:<host_port>]' instead.");
            outbound_port = par.getInt();
            return;
        }
    }

    /** Converts the entire object into lines (to be saved into the config file) */
    protected String toLines()
    { // currently not implemented..
        return toString();
    }

    /** Gets a String with the list of transport protocols. */
    private String transportProtocolsToString()
    {
        String list = transport_protocols[0];
        for (int i = 1; i < transport_protocols.length; i++)
            list += "/" + transport_protocols[i];
        return list;
    }

    // ************************** Public methods *************************

    /** Gets via address. */
    public String getViaAddress()
    {
        return via_addr;
    }

    /** Sets via address. */
    public void setViaAddress(String addr)
    {
        via_addr = addr;
    }

    /** Gets host port. */
    public int getPort()
    {
        return host_port;
    }

    /**
     * Whether binding the sip provider to all interfaces or only on the
     * specified host address.
     */
    public boolean isAllInterfaces()
    {
        return host_ipaddr == null;
    }

    /** Gets host interface IpAddress. */
    public IpAddress getInterfaceAddress()
    {
        return host_ipaddr;
    }

    /** Gets array of transport protocols. */
    public String[] getTransportProtocols()
    {
        return transport_protocols;
    }

    /** Gets the default transport protocol. */
    public String getDefaultTransport()
    {
        return default_transport;
    }

    /** Gets the default transport protocol. */
    public void setDefaultTransport(String proto)
    {
        default_transport = proto;
    }

    /** Sets rport support. */
    public void setRport(boolean flag)
    {
        rport = flag;
    }

    /** Whether using rport. */
    public boolean isRportSet()
    {
        return rport;
    }

    /** Sets 'force-rport' mode. */
    public void setForceRport(boolean flag)
    {
        force_rport = flag;
    }

    /** Whether using 'force-rport' mode. */
    public boolean isForceRportSet()
    {
        return force_rport;
    }

    /** Whether has outbound proxy. */
    public boolean hasOutboundProxy()
    {
        return outbound_proxy != null;
    }

    /** Gets the outbound proxy. */
    public SocketAddress getOutboundProxy()
    {
        return outbound_proxy;
    }

    /** Sets the outbound proxy. Use 'null' for not using any outbound proxy. */
    public void setOutboundProxy(SocketAddress soaddr)
    {
        outbound_proxy = soaddr;
    }

    /** Removes the outbound proxy. */
    /*
     * public void removeOutboundProxy() { setOutboundProxy(null); }
     */

    /** Gets the max number of (contemporary) open connections. */
    public int getNMaxConnections()
    {
        return nmax_connections;
    }

    /** Sets the max number of (contemporary) open connections. */
    public void setNMaxConnections(int n)
    {
        nmax_connections = n;
    }

    /** Returns the list (Hashtable) of active listener_IDs. */
    public Hashtable<Identifier, SipProviderListener> getListeners()
    {
        return listeners;
    }

    /**
     * Adds a new listener to the SipProvider for caputering any message in
     * PROMISQUE mode. It is the same as using method
     * addSipProviderListener(SipProvider.PROMISQUE,listener).
     * <p/>
     * When capturing messages in promisque mode all messages are passed to the
     * SipProviderListener before passing them to the specific listener (if
     * present). <br/>
     * Note that more that one SipProviderListener can be active in promisque
     * mode at the same time;in that case the same message is passed to all
     * PROMISQUE SipProviderListeners.
     * 
     * @param listener
     *            is the SipProviderListener.
     * @return It returns <i>true</i> if the SipProviderListener is added,
     *         <i>false</i> if the listener_ID is already in use.
     */
    public boolean addSipProviderPromisqueListener(SipProviderListener listener)
    {
        return addSipProviderListener(PROMISQUE, listener);
    }

    /**
     * Adds a new listener to the SipProvider for caputering ANY message. It is
     * the same as using method
     * addSipProviderListener(SipProvider.ANY,listener).
     * 
     * @param listener
     *            is the SipProviderListener.
     * @return It returns <i>true</i> if the SipProviderListener is added,
     *         <i>false</i> if the listener_ID is already in use.
     */
    public boolean addSipProviderListener(SipProviderListener listener)
    {
        return addSipProviderListener(ANY, listener);
    }

    /**
     * Adds a new listener to the SipProvider.
     * 
     * @param id
     *            is the unique identifier for the messages which the listener
     *            as to be associated to. It is used as key. It can identify a
     *            method, a transaction, or a dialog. Use SipProvider.ANY to
     *            capture all messages. Use SipProvider.PROMISQUE if you want to
     *            capture all message in promisque mode (letting other listeners
     *            to capture the same received messages).
     * @param listener
     *            is the SipProviderListener for this message id.
     * @return It returns <i>true</i> if the SipProviderListener is added,
     *         <i>false</i> if the listener_ID is already in use.
     */
    public boolean addSipProviderListener(Identifier id, SipProviderListener listener)
    {
        Log.info("SipProvider", "adding SipProviderListener: " + id);
        boolean ret;
        Identifier key = id;

        if (listeners.containsKey(key))
        {
            Log.error("SipProvider", "trying to add a SipProviderListener with a id that is already in use.");
            ret = false;
        }
        else
        {
            listeners.put(key, listener);
            ret = true;
        }

        // if (listeners != null) {
        // String list = "";
        // for (Enumeration<Identifier> e = listeners.keys(); e
        // .hasMoreElements();)
        // list += e.nextElement() + ", ";
        // Log.info("SipProvider", listeners.size() + " listeners: " + list);
        // }
        return ret;
    }

    /**
     * Removes a SipProviderListener.
     * 
     * @param id
     *            is the unique identifier used to select the listened messages.
     * @return It returns <i>true</i> if the SipProviderListener is removed,
     *         <i>false</i> if the identifier is missed.
     */
    public boolean removeSipProviderListener(Identifier id)
    {
        Log.info("SipProvider", "removing SipProviderListener: " + id);
        boolean ret;
        Identifier key = id;
        if (!listeners.containsKey(key))
        {
            Log.error("SipProvider", "trying to remove a missed SipProviderListener.");
            ret = false;
        }
        else
        {
            listeners.remove(key);
            ret = true;
        }

        /**
         * if (listeners != null) { String list = ""; for
         * (Enumeration<Identifier> e = listeners.keys(); e .hasMoreElements();)
         * list += e.nextElement() + ", "; Log.info("SipProvider",
         * listeners.size() + " listeners: " + list); }
         */
        return ret;
    }

    /**
     * Sets the SipProviderExceptionListener. The SipProviderExceptionListener
     * is the listener for all exceptions thrown by the SipProviders.
     * 
     * @param e_listener
     *            is the SipProviderExceptionListener.
     * @return It returns <i>true</i> if the SipProviderListener has been
     *         correctly set, <i>false</i> if the SipProviderListener was
     *         already set.
     */
    public boolean addSipProviderExceptionListener(SipProviderExceptionListener e_listener)
    {
        Log.info("SipProvider", "adding SipProviderExceptionListener");
        if (exception_listeners.contains(e_listener))
        {
            Log.error("SipProvider", "trying to add an already present SipProviderExceptionListener.");
            return false;
        }
        else
        {
            exception_listeners.add(e_listener);
            return true;
        }
    }

    /**
     * Removes a SipProviderExceptionListener.
     * 
     * @param e_listener
     *            is the SipProviderExceptionListener.
     * @return It returns <i>true</i> if the SipProviderExceptionListener has
     *         been correctly removed, <i>false</i> if the
     *         SipProviderExceptionListener is missed.
     */
    public boolean removeSipProviderExceptionListener(SipProviderExceptionListener e_listener)
    {
        Log.info(TAG, "removing SipProviderExceptionListener");
        if (!exception_listeners.contains(e_listener))
        {
            Log.error(TAG, "trying to remove a missed SipProviderExceptionListener.");
            return false;
        }
        else
        {
            exception_listeners.remove(e_listener);
            return true;
        }
    }

    /**
     * Sends a Message, specifing the transport portocol, nexthop address and
     * port.
     * <p>
     * This is a low level method and forces the message to be routed to a
     * specific nexthop address, port and transport, regardless whatever the
     * Via, Route, or request-uri, address to.
     * <p>
     * In case of connection-oriented transport, the connection is selected as
     * follows: <br>
     * - if an existing connection is found matching the destination end point
     * (socket), such connection is used, otherwise <br>
     * - a new connection is established
     * 
     * @return It returns a Connection in case of connection-oriented delivery
     *         (e.g. TCP) or null in case of connection-less delivery (e.g. UDP)
     */
    public ConnectionIdentifier sendMessage(Message msg, String proto, String dest_addr, int dest_port, int ttl)
    {
        try
        {
            IpAddress dest_ipaddr = IpAddress.getByName(dest_addr);
            Log.info(TAG, "sendMessage 01 dest_ipaddr:" + dest_ipaddr);
            return sendMessage(msg, proto, dest_ipaddr, dest_port, ttl);
        }
        catch (Exception e)
        {
            Log.exception("SipProvider.sendMessage 01", e);
            return null;
        }
    }

    /**
     * Sends a Message, specifing the transport portocol, nexthop address and
     * port.
     */
    private ConnectionIdentifier sendMessage(Message msg, String proto, IpAddress dest_ipaddr, int dest_port, int ttl)
    {
        ConnectionIdentifier conn_id = new ConnectionIdentifier(proto, dest_ipaddr, dest_port);
        if (log_all_packets || msg.getLength() > MIN_MESSAGE_LENGTH)
            Log.info(TAG, "Sending message to " + conn_id);

        if (transport_udp && proto.equals(PROTO_UDP))
        { // UDP
            try
            { // if (ttl>0 && multicast_address) do something?
                if (udp == null)
                {
                    Log.error(TAG, "sendMessage udp is null");
                }
                udp.sendMessage(msg, dest_ipaddr, dest_port);
                Log.info(TAG, "Send message success.");
            }
            catch (Exception e)
            {
                Log.error("SipProvider.sendMessage 02", "dest_ipaddr:" + dest_ipaddr.toString() + " dest_port:" + dest_port);
                Log.exception("SipProvider.sendMessage 02", e);
                return null;
            }
        }
        else if (transport_tcp && proto.equals(PROTO_TCP))
        { // TCP
            if (connections == null || !connections.containsKey(conn_id))
            { // modified
                Log.info(TAG, "no active connection found matching " + conn_id);
                Log.info(TAG, "open " + proto + " connection to " + dest_ipaddr + ":" + dest_port);

                TcpTransport conn = null;
                try
                {
                    conn = new TcpTransport(dest_ipaddr, dest_port, this);
                }
                catch (Exception e)
                {
                    Log.error(TAG, "connection setup FAILED ");
                    Log.exception("SipProvider.sendMessage 02", e);
                    return null;
                }
                Log.info(TAG, "connection " + conn + " opened");
                addConnection(conn);
            }
            else
            {
                Log.info(TAG, "active connection found matching " + conn_id);
            }
            ConnectedTransport conn = (ConnectedTransport) connections.get(conn_id);
            if (conn != null)
            {
                Log.info(TAG, "sending data through conn " + conn);
                try
                {
                    conn.sendMessage(msg);
                    conn_id = new ConnectionIdentifier(conn);
                }
                catch (IOException e)
                {
                    Log.exception("SipProvider.sendMessage 02", e);
                    return null;
                }
            }
            else
            { // this point has not to be reached
                Log.error(TAG, "ERROR: conn " + conn_id + " not found: abort.");
                return null;
            }
        }
        else
        { // otherwise
            Log.error(TAG, "Unsupported protocol (" + proto + "): Message discarded");
            return null;
        }
        // logs
        return conn_id;
    }

    /**
     * Sends the message <i>msg</i>.
     * <p>
     * The destination for the request is computed as follows: <br>
     * - if <i>outbound_addr</i> is set, <i>outbound_addr</i> and
     * <i>outbound_port</i> are used, otherwise <br>
     * - if message has Route header with lr option parameter (i.e. RFC3261
     * compliant), the first Route address is used, otherwise <br>
     * - the request's Request-URI is considered.
     * <p>
     * The destination for the response is computed based on the sent-by
     * parameter in the Via header field (RFC3261 compliant)
     * <p>
     * As transport it is used the protocol specified in the 'via' header field
     * <p>
     * In case of connection-oriented transport: <br>
     * - if an already established connection is found matching the destination
     * end point (socket), such connection is used, otherwise <br>
     * - a new connection is established
     * 
     * @return Returns a ConnectionIdentifier in case of connection-oriented
     *         delivery (e.g. TCP) or null in case of connection-less delivery
     *         (e.g. UDP)
     * @throws Exception 
     */
    public ConnectionIdentifier sendMessage(Message msg) throws Exception
    {
        ViaHeader via = msg.getViaHeader();
        String proto;
        if (via != null)
            proto = via.getProtocol().toLowerCase();
        else
            proto = getDefaultTransport().toLowerCase(); // modified
        int dest_port = 0;
        int ttl = 0;

        // if (!msg.isResponse()) { // modified
        // if (outbound_proxy != null) {
        // dest_addr = outbound_proxy.getAddress().toString();
        // dest_port = outbound_proxy.getPort();
        // } else {
        // if (msg.hasRouteHeader()
        // && msg.getRouteHeader().getNameAddress().getAddress()
        // .hasLr()) {
        // SipURL url = msg.getRouteHeader().getNameAddress()
        // .getAddress();
        // dest_addr = url.getHost();
        // dest_port = url.getPort();
        // } else {
        // SipURL url = msg.getRequestLine().getAddress();
        // dest_addr = url.getHost();
        // dest_port = url.getPort();
        // if (url.hasMaddr()) {
        // dest_addr = url.getMaddr();
        // if (url.hasTtl())
        // ttl = url.getTtl();
        // // update the via header by adding maddr and ttl params
        // via.setMaddr(dest_addr);
        // if (ttl > 0)
        // via.setTtl(ttl);
        // msg.removeViaHeader();
        // msg.addViaHeader(via);
        // }
        // }
        // }
        // } else { // RESPONSES
        // SipURL url = via.getSipURL();
        // if (via.hasReceived())
        // dest_addr = via.getReceived();
        // else
        // dest_addr = url.getHost();
        // if (via.hasRport())
        // dest_port = via.getRport();
        // if (dest_port <= 0)
        // dest_port = url.getPort();
        // }
        //
        // if (dest_port <= 0)
        // dest_port = SipStack.default_port;

        if (TextUtils.isEmpty(destAddr))
        {
            throw new Exception("no dest_addr information");
        }
        Log.info(TAG, "dest_addr:" + destAddr + " dest_port:" + destPort);
        return sendMessage(msg, proto, destAddr, destPort, ttl);
    }

    /** Sends the message <i>msg</i> using the specified connection. 
     * @throws Exception */
    public ConnectionIdentifier sendMessage(Message msg, ConnectionIdentifier conn_id) throws Exception
    {
        Log.info(TAG, "Sending a message through conn " + conn_id);

        if (conn_id != null && connections.containsKey(conn_id))
        { // connection
            // exists
            Log.info(TAG, "active connection found matching " + conn_id);
            ConnectedTransport conn = (ConnectedTransport) connections.get(conn_id);
            try
            {
                conn.sendMessage(msg);
                return conn_id;
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
        return sendMessage(msg);
    }

    /**
     * Processes the message received. It is called each time a new message is
     * received by the transport layer, and it performs the actual message
     * processing.
     */
    protected void processReceivedMessage(Message msg)
    {
        try
        {
            // discard too short messages
            if (msg.getLength() <= 2)
            {
                Log.info(TAG, "message too short: discarded");
                return;
            }
            // discard non-SIP messages
            String first_line = msg.getFirstLine();
            if (first_line == null || first_line.toUpperCase().indexOf("SIP/2.0") < 0)
            {
                Log.info(TAG, "NOT a SIP message: discarded");
                return;
            }
            Log.info(TAG, "received new SIP message ");

            // if a request, handle "received" and "rport" parameters
            if (msg.isRequest())
            {
                ViaHeader vh = msg.getViaHeader();
                boolean via_changed = false;
                String src_addr = msg.getRemoteAddress();
                int src_port = msg.getRemotePort();
                String via_addr = vh.getHost();
                int via_port = vh.getPort();
                if (via_port <= 0)
                    via_port = SipStack.default_port;

                if (!via_addr.equals(src_addr))
                {
                    vh.setReceived(src_addr);
                    via_changed = true;
                }

                if (vh.hasRport())
                {
                    vh.setRport(src_port);
                    via_changed = true;
                }
                else
                {
                    if (force_rport && via_port != src_port)
                    {
                        vh.setRport(src_port);
                        via_changed = true;
                    }
                }

                if (via_changed)
                {
                    msg.removeViaHeader();
                    msg.addViaHeader(vh);
                }
            }

            // is there any listeners?
            if (listeners == null || listeners.size() == 0)
            {
                Log.info(TAG, "no listener found: message discarded.");
                return;
            }

            // try to look for a UA in promisque mode
            if (listeners.containsKey(PROMISQUE))
            {
                Log.info(TAG, "message passed to uas: " + PROMISQUE);
                ((SipProviderListener) listeners.get(PROMISQUE)).onReceivedMessage(this, msg);
            }

            // after the callback check if the message is still valid
            if (!msg.isRequest() && !msg.isResponse())
            {
                Log.info(TAG, "No valid SIP message: message discarded.");
                return;
            }

            // try to look for a transaction
            Identifier key = msg.getTransactionId();
            Log.info(TAG, "info: transaction-id: " + key);
            if (listeners.containsKey(key))
            {
                Log.info(TAG, "message passed to transaction: " + key);
                SipProviderListener sip_listener = (SipProviderListener) listeners.get(key);
                if (sip_listener != null)
                {
                    sip_listener.onReceivedMessage(this, msg);
                }
                return;
            }
            // try to look for a dialog
            key = msg.getDialogId();
            Log.info(TAG, "info: dialog-id: " + key);
            if (listeners.containsKey(key))
            {
                Log.info(TAG, "message passed to dialog: " + key);
                ((SipProviderListener) listeners.get(key)).onReceivedMessage(this, msg);
                return;
            }
            // try to look for a UAS
            key = msg.getMethodId();
            Log.info(TAG, "info: uas-id: " + key);
            if (listeners.containsKey(key))
            {
                Log.info(TAG, "message passed to uas: " + key);
                ((SipProviderListener) listeners.get(key)).onReceivedMessage(this, msg);
                return;
            }
            // try to look for a default UA
            if (listeners.containsKey(ANY))
            {
                Log.info(TAG, "message passed to uas: " + ANY);
                ((SipProviderListener) listeners.get(ANY)).onReceivedMessage(this, msg);
                return;
            }
        }
        catch (Exception e)
        {
            Log.error(TAG, "Error handling a new incoming message ");
            e.printStackTrace();
            Log.exception(TAG, e);
            if (exception_listeners == null || exception_listeners.size() == 0)
            {
                Log.error(TAG, "Error handling a new incoming message");
            }
            else
            {
                for (Iterator i = exception_listeners.iterator(); i.hasNext();)
                    try
                    {
                        ((SipProviderExceptionListener) i.next()).onMessageException(msg, e);
                    }
                    catch (Exception e2)
                    {
                        Log.error(TAG, "Error handling handling the Exception ");
                        Log.exception(TAG, e2);
                    }
            }
        }
    }

    /** Adds a new Connection */
    private void addConnection(ConnectedTransport conn)
    {
        ConnectionIdentifier conn_id = new ConnectionIdentifier(conn);
        if (connections.containsKey(conn_id))
        { // remove the previous
            // connection
            Log.info(TAG, "trying to add the already established connection " + conn_id);
            ConnectedTransport old_conn = (ConnectedTransport) connections.get(conn_id);
            old_conn.halt();
            connections.remove(conn_id);
        }
        else if (connections.size() >= nmax_connections)
        { // remove the
            // older unused
            // connection
            Log.info(TAG, "reached the maximum number of connection: removing the older unused connection");
            long older_time = System.currentTimeMillis();
            ConnectionIdentifier older_id = null;
            for (Enumeration<ConnectedTransport> e = connections.elements(); e.hasMoreElements();)
            {
                ConnectedTransport co = e.nextElement();
                if (co.getLastTimeMillis() < older_time)
                    older_id = new ConnectionIdentifier(co);
            }
            if (older_id != null)
                removeConnection(older_id);
        }
        connections.put(conn_id, conn);
        conn_id = new ConnectionIdentifier(conn);
        conn = (ConnectedTransport) connections.get(conn_id);
        // info log:
        Log.info(TAG, "active connenctions:");
        for (Enumeration<ConnectionIdentifier> e = connections.keys(); e.hasMoreElements();)
        {
            ConnectionIdentifier id = (ConnectionIdentifier) e.nextElement();
            Log.info(TAG, "conn-id=" + id + ": " + ((ConnectedTransport) connections.get(id)).toString());
        }
    }

    /** Removes a Connection */
    private void removeConnection(ConnectionIdentifier conn_id)
    {
        if (connections != null && connections.containsKey(conn_id))
        { // modified
            ConnectedTransport conn = (ConnectedTransport) connections.get(conn_id);
            if (conn != null)
                conn.halt();
            connections.remove(conn_id);
            // info log:
            Log.info(TAG, "active connenctions:");
            for (Enumeration<ConnectedTransport> e = connections.elements(); e.hasMoreElements();)
            {
                ConnectedTransport co = (ConnectedTransport) e.nextElement();
                Log.info(TAG, "conn " + co.toString());
            }
        }
    }

    // ************************* Callback methods *************************

    /** When a new SIP message is received. */
    public void onReceivedMessage(Transport transport, final Message msg)
    {
        try
        {
            processReceivedMessage(msg);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /** When Transport terminates. */
    public void onTransportTerminated(Transport transport, Exception error)
    {
        Log.info(TAG, "transport " + transport + " terminated");
        if (transport.getProtocol().equals(PROTO_TCP))
        {
            ConnectionIdentifier conn_id = new ConnectionIdentifier((ConnectedTransport) transport);
            removeConnection(conn_id);
        }
        if (error != null)
            Log.exception(TAG, error);
    }

    /** When a new incoming Connection is established */
    public void onIncomingConnection(TcpServer tcp_server, TcpSocket socket)
    {
        Log.info(TAG, "incoming connection from " + socket.getAddress() + ":" + socket.getPort());
        ConnectedTransport conn = new TcpTransport(socket, this);
        Log.info(TAG, "tcp connection " + conn + " opened");
        addConnection(conn);
    }

    /** When TcpServer terminates. */
    public void onServerTerminated(TcpServer tcp_server, Exception error)
    {
        Log.info(TAG, "tcp server " + tcp_server + " terminated");
    }

    // ************************** Other methods ***************************

    /**
     * Picks a fresh branch value. The branch ID MUST be unique across space and
     * time for all requests sent by the UA. The branch ID always begin with the
     * characters "z9hG4bK". These 7 characters are used by RFC 3261 as a magic
     * cookie.
     */
    public static String pickBranch()
    { // String
        // str=Long.toString(Math.abs(Random.nextLong()),16);
        // if (str.length()<5) str+="00000";
        // return "z9hG4bK"+str.substring(0,5);
        return "z9hG4bK" + Random.nextNumString(5);
    }

    /**
     * Picks an unique branch value based on a SIP message. This value could
     * also be used as transaction ID
     */
    public String pickBranch(Message msg)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(msg.getRequestLine().getAddress().toString());
        sb.append(getViaAddress() + getPort());
        ViaHeader top_via = msg.getViaHeader();
        if (top_via.hasBranch())
            sb.append(top_via.getBranch());
        else
        {
            sb.append(top_via.getHost() + top_via.getPort());
            sb.append(msg.getCSeqHeader().getSequenceNumber());
            sb.append(msg.getCallIdHeader().getCallId());
            sb.append(msg.getFromHeader().getTag());
            sb.append(msg.getToHeader().getTag());
        }
        // return "z9hG4bK"+(new MD5(unique_str)).asHex().substring(0,9);
        return "z9hG4bK" + (new SimpleDigest(5, sb.toString())).asHex();
    }

    /**
     * Picks a new tag. A tag MUST be globally unique and cryptographically
     * random with at least 32 bits of randomness. A property of this selection
     * requirement is that a UA will place a different tag into the From header
     * of an INVITE than it would place into the To header of the response to
     * the same INVITE. This is needed in order for a UA to invite itself to a
     * session.
     */
    public static String pickTag()
    { // String
        // str=Long.toString(Math.abs(Random.nextLong()),16);
        // if (str.length()<8) str+="00000000";
        // return str.substring(0,8);
        return "z9hG4bK" + Random.nextNumString(8);
    }

    /**
     * Picks a new tag. The tag is generated uniquely based on message
     * <i>req</i>. This tag can be generated for responses in a stateless manner
     * - in a manner that will generate the same tag for the same request
     * consistently.
     */
    public static String pickTag(Message req)
    { // return
        // String.valueOf(tag_generator++);
        // return (new MD5(request.toString())).asHex().substring(0,8);
        return (new SimpleDigest(8, req.toString())).asHex();
    }

    /**
     * Picks a new call-id. The call-id is a globally unique identifier over
     * space and time. It is implemented in the form "localid@host". Call-id
     * must be considered case-sensitive and is compared byte-by-byte.
     */
    public String pickCallId()
    { // String
        // str=Long.toString(Math.abs(Random.nextLong()),16);
        // if (str.length()<12) str+="000000000000";
        // return str.substring(0,12)+"@"+getViaAddress();

        return Random.nextNumString(12) + "@" + getViaAddress();
    }

    public String pickRegCallId(String user)
    { // String
        // str=Long.toString(Math.abs(Random.nextLong()),16);
        // if (str.length()<12) str+="000000000000";
        // return str.substring(0,12)+"@"+getViaAddress();

        return user + "@" + getViaAddress();
    }

    /** picks an initial CSeq */
    public static int pickInitialCSeq()
    {
        return 1;
    }

    /**
     * (<b>Deprecated</b>) Constructs a NameAddress based on an input string.
     * The input string can be a: <br>
     * - <i>user</i> name, <br>
     * - <i>user@address</i> url, <br>
     * - <i>"Name" &lt;sip:user@address&gt;</i> address,
     * <p>
     * In the former case, a SIP URL is costructed using the outbound proxy as
     * host address if present, otherwise the local via address is used.
     */
    public NameAddress completeNameAddress(String str)
    {
        if (str.indexOf("<sip:") >= 0)
            return new NameAddress(str);
        else
        {
            SipURL url = completeSipURL(str);
            return new NameAddress(url);
        }
    }

    /** Constructs a SipURL based on an input string. */
    private SipURL completeSipURL(String str)
    { // in case it is passed only the
        // 'user' field, add
        // '@'<outbound_proxy>[':'<outbound_port>]
        if (!str.startsWith("sip:") && str.indexOf("@") < 0 && str.indexOf(".") < 0 && str.indexOf(":") < 0)
        { // may be it
            // is just
            // the user
            // name..
            String url = "sip:" + str + "@";
            if (outbound_proxy != null)
            {
                url += outbound_proxy.getAddress().toString();
                int port = outbound_proxy.getPort();
                if (port > 0 && port != SipStack.default_port)
                    url += ":" + port;
            }
            else
            {
                url += getViaAddress();
                if (host_port > 0 && host_port != SipStack.default_port)
                    url += ":" + host_port;
            }
            return new SipURL(url);
        }
        else
            return new SipURL(str);
    }

    /**
     * Constructs a SipURL for the given <i>username</i> on the local SIP UA. If
     * <i>username</i> is null, only host address and port are used.
     */
    /*
     * public SipURL getSipURL(String user_name) { return new
     * SipURL(user_name,via_addr
     * ,(host_port!=SipStack.default_port)?host_port:-1); }
     */

    /** Gets a String value for this object */
    public String toString()
    {
        if (host_ipaddr == null)
            return host_port + "/" + transportProtocolsToString();
        else
            return host_ipaddr.toString() + ":" + host_port + "/" + transportProtocolsToString();
    }

    public void setDestAddr(String destAddr)
    {
        this.destAddr = destAddr;
    }
    
    public String getDestAddr()
    {
        return destAddr;
    }

    public int getDestPort()
    {
        return destPort;
    }

    public void setDestPort(int destPort)
    {
        this.destPort = destPort;
    }

    public String getOrigAddr()
    {
        return origAddr;
    }

    public void setOrigAddr(String origAddr)
    {
        this.origAddr = origAddr;
    }
}
