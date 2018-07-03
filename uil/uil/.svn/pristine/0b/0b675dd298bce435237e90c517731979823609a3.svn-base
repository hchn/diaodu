package com.jiaxun.uil.util.eventnotify;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Handler;

import com.jiaxun.uil.UiApplication;

public class EventNotifyHelper
{
    final private HashMap<Integer, List<Object>> observers = new HashMap<Integer, List<Object>>();

//    final private HashMap<String, Object> memCache = new HashMap<String, Object>();

    final private HashMap<Integer, Object> removeAfterBroadcast = new HashMap<Integer, Object>();
    final private HashMap<Integer, Object> addAfterBroadcast = new HashMap<Integer, Object>();

    private boolean broadcasting = false;

    private static volatile EventNotifyHelper Instance = null;

    public static EventNotifyHelper getInstance()
    {
        EventNotifyHelper localInstance = Instance;
        if (localInstance == null)
        {
            synchronized (EventNotifyHelper.class)
            {
                localInstance = Instance;
                if (localInstance == null)
                {
                    Instance = localInstance = new EventNotifyHelper();
                }
            }
        }
        return localInstance;
    }

    public interface NotificationCenterDelegate
    {
        public abstract void didReceivedNotification(int id, Object... args);
    }

    public void postUiNotification(final int id, final Object... args)
    {
        synchronized (observers)
        {
            broadcasting = true;
            final List<Object> objects = observers.get(id);
            if (objects != null)
            {
                new Handler(UiApplication.getInstance().getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (Object obj : objects)
                        {
                            ((NotificationCenterDelegate) obj).didReceivedNotification(id, args);
                        }
                    }
                });
            }
            broadcasting = false;
            if (!removeAfterBroadcast.isEmpty())
            {
                for (HashMap.Entry<Integer, Object> entry : removeAfterBroadcast.entrySet())
                {
                    removeObserver(entry.getValue(), entry.getKey());
                }
                removeAfterBroadcast.clear();
            }
            if (!addAfterBroadcast.isEmpty())
            {
                for (HashMap.Entry<Integer, Object> entry : addAfterBroadcast.entrySet())
                {
                    addObserver(entry.getValue(), entry.getKey());
                }
                addAfterBroadcast.clear();
            }
        }
    }

    public void postNotificationName(int id, Object... args)
    {
        synchronized (observers)
        {
            broadcasting = true;
            List<Object> objects = observers.get(id);
            if (objects != null)
            {
                for (Iterator iterator = objects.iterator(); iterator.hasNext();)
                {
                    Object object = (Object) iterator.next();
                    ((NotificationCenterDelegate) object).didReceivedNotification(id, args);
                }
            }
            broadcasting = false;
            if (!removeAfterBroadcast.isEmpty())
            {
                for (HashMap.Entry<Integer, Object> entry : removeAfterBroadcast.entrySet())
                {
                    removeObserver(entry.getValue(), entry.getKey());
                }
                removeAfterBroadcast.clear();
            }
            if (!addAfterBroadcast.isEmpty())
            {
                for (HashMap.Entry<Integer, Object> entry : addAfterBroadcast.entrySet())
                {
                    addObserver(entry.getValue(), entry.getKey());
                }
                addAfterBroadcast.clear();
            }
        }
    }

    public void addObserver(Object observer, int id)
    {
        synchronized (observers)
        {
            if (broadcasting)
            {
                addAfterBroadcast.put(id, observer);
                return;
            }
            List<Object> objects = observers.get(id);
            if (objects == null)
            {
                observers.put(id, (objects = new CopyOnWriteArrayList<Object>()));
            }
            if (objects.contains(observer))
            {
                return;
            }
            // if( id == MessagesController.contactsDidLoaded)
            // FileLog.d("emm", "postNotificationName addObserver");
            objects.add(observer);
        }
    }

    public void removeObserver(Object observer, int id)
    {
        synchronized (observers)
        {
            if (broadcasting)
            {
                removeAfterBroadcast.put(id, observer);
                return;
            }
            List<Object> objects = observers.get(id);
            if (objects != null)
            {
                objects.remove(observer);
                if (objects.size() == 0)
                {
                    observers.remove(id);
                }
            }
        }
    }
}
