/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.phantom.eventbus;

import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus implements IEventBus {
    private final Map<Class<? extends PhantomEvent>, CopyOnWriteArrayList<Listener>> listeners = new ConcurrentHashMap<>();

    /**
     * Registers a object.
     *
     * @param registerClass object to register
     */
    @Override
    public void register(Object registerClass) {
        Arrays.stream(registerClass.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(PhantomSubscribe.class))
                .filter(method -> method.getParameterCount() == 1)
                .forEach(method -> {
                    if (!method.canAccess(registerClass)) method.setAccessible(true);

                    @SuppressWarnings("unchecked") Class<? extends PhantomEvent> event =
                            (Class<? extends PhantomEvent>) method.getParameterTypes()[0];

                    Consumer<PhantomEvent> lambda = null;
                    if (method.getDeclaredAnnotation(PhantomSubscribe.class).lambda())
                        lambda = getLambda(registerClass, method, event);
                    if (!listeners.containsKey(event)) listeners.put(event, new CopyOnWriteArrayList<>());

                    listeners.get(event).add(new Listener(registerClass, method, lambda));
                });
    }

    /**
     * Unregisters a object.
     *
     * @param registerClass object to unregister
     */
    @Override
    public void unregister(Object registerClass) {
        listeners.values().forEach(arrayList -> arrayList.removeIf(listener -> listener.getListenerClass().equals(registerClass)));
    }

    /**
     * Posts an event.
     *
     * @param event event to post
     */
    @Override
    public void post(PhantomEvent event) {
        List<Listener> listenersList = listeners.get(event.getClass());
        if (listenersList != null) for (Listener listener : listenersList) {
            if (event.isCancelled()) return;
            if (listener.getLambda() != null)
                listener.getLambda().accept(event);
            else {
                try {
                    listener.getMethod().invoke(listener.getListenerClass(), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets the lambda of the listener.
     *
     * @param object object
     * @param method method
     * @param event  event
     * @return event lambda
     */
    protected Consumer<PhantomEvent> getLambda(Object object, Method method, Class<? extends PhantomEvent> event) {
        Consumer<PhantomEvent> eventLambda = null;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType subscription = MethodType.methodType(void.class, event);
            MethodHandle target = lookup.findVirtual(object.getClass(), method.getName(), subscription);
            CallSite site = LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    MethodType.methodType(Consumer.class, object.getClass()),
                    subscription.changeParameterType(0, Object.class),
                    target,
                    subscription);

            MethodHandle factory = site.getTarget();
            eventLambda = (Consumer<PhantomEvent>) factory.bindTo(object).invokeExact();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return eventLambda;
    }
}
