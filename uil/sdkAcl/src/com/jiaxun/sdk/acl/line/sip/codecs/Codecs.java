/*
 * Copyright (C) 2010 The Sipdroid Open Source Project
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.jiaxun.sdk.acl.line.sip.codecs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import org.zoolu.sdp.AttributeField;
import org.zoolu.sdp.MediaField;
import org.zoolu.sdp.SessionDescriptor;

public class Codecs
{
    private static final Vector<Codec> codecs = new Vector<Codec>()
    {
        {
            // add(new G722());
            // add(new SILK24()); save space (until a common library for all
            // bitrates gets available?)
            // add(new SILK16());
            // add(new SILK8());
            // add(new G729A());
            add(new OPUS());
            // add(new alaw());
            // add(new ulaw());
            // add(new Speex());
            // add(new GSM());
            // add(new BV16());
        }
    };
    private static final HashMap<Integer, Codec> codecsNumbers;
    private static final HashMap<String, Codec> codecsNames;

    static
    {
        final int size = codecs.size();
        codecsNumbers = new HashMap<Integer, Codec>(size);
        codecsNames = new HashMap<String, Codec>(size);

        for (Codec c : codecs)
        {
            codecsNames.put(c.name(), c);
            codecsNumbers.put(c.number(), c);
        }
    }

    public static Codec get(int key)
    {
        return codecsNumbers.get(key);
    }

    public static Codec getName(String name)
    {
        return codecsNames.get(name);
    }

    public static int[] getCodecs()
    {
        Vector<Integer> v = new Vector<Integer>(codecs.size());

        for (Codec c : codecs)
        {
            c.update();
            if (!c.isValid())
                continue;
            v.add(c.number());
        }
        int i[] = new int[v.size()];
        for (int j = 0; j < i.length; j++)
            i[j] = v.elementAt(j);
        return i;
    }

    public static class Map implements Serializable
    {
        private static final long serialVersionUID = 1L;
        public int number;
        public Codec codec;
        Vector<Integer> numbers;
        Vector<Codec> codecs;

        Map(int n, Codec c, Vector<Integer> ns, Vector<Codec> cs)
        {
            number = n;
            codec = c;
            numbers = ns;
            codecs = cs;
        }

        public boolean change(int n)
        {
            int i = numbers.indexOf(n);

            if (i >= 0 && codecs.elementAt(i) != null)
            {
                codec.close();
                number = n;
                codec = codecs.elementAt(i);
                return true;
            }
            return false;
        }

        public String toString()
        {
            return "Codecs.Map { " + number + ": " + codec + "}";
        }
    }

    /**
     * 协商获取语音编码
     */
    public static Map getCodec(SessionDescriptor offers)
    {
        MediaField m = null;
        try
        {
            m = offers.getMediaDescriptor("audio").getMedia();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if (m == null)
            return null;

        String proto = m.getTransport();
        // see http://tools.ietf.org/html/rfc4566#page-22, paragraph 5.14, <fmt>
        // description
        if (proto.equals("RTP/AVP") || proto.equals("RTP/SAVP"))
        {
            Vector<String> formats = m.getFormatList();
            Vector<String> names = new Vector<String>(formats.size());
            Vector<Integer> numbers = new Vector<Integer>(formats.size());
            Vector<Codec> codecmap = new Vector<Codec>(formats.size());

            // add all avail formats with empty names
            for (String fmt : formats)
            {
                try
                {
                    int number = Integer.parseInt(fmt);
                    numbers.add(number);
                    names.add("");
                    codecmap.add(null);
                }
                catch (NumberFormatException e)
                {
                    // continue ... remote sent bogus rtp setting
                }
            }

            // if we have attrs for format -> set name
            Vector<AttributeField> attrs = offers.getMediaDescriptor("audio").getAttributes("rtpmap");
            for (AttributeField a : attrs)
            {
                String s = a.getValue();
                // skip over "rtpmap:"
                s = s.substring(7, s.indexOf("/"));
                int i = s.indexOf(" ");
                try
                {
                    String name = s.substring(i + 1);
                    int number = Integer.parseInt(s.substring(0, i));
                    int index = numbers.indexOf(number);
                    if (index >= 0)
                        names.set(index, name.toLowerCase());
                }
                catch (NumberFormatException e)
                {
                    // continue ... remote sent bogus rtp setting
                }
            }

            for (Codec c : codecs)
            {// 初始化本地的编码库
                c.update();
                if (!c.isValid())
                    continue;

                // search current codec in offers by name
                int i = names.indexOf(c.userName().toLowerCase());
                if (i >= 0)
                {
                    codecmap.set(i, c);
                }
            }

            Codec codec = null;
            Integer j = 0;
            for (Integer i : numbers)
            {
                for (Codec c : codecs)
                {
                    if (i.intValue() == c.number())
                    {
                        j = i;
                        codec = c;
                        break;
                    }
                }
            }

            if (codec != null)
            {
                return new Map(j.intValue(), codec, numbers, codecmap);
            }
            else
            {
                return null;
            }
        }
        else
        {

            return null;
        }
    }
}
