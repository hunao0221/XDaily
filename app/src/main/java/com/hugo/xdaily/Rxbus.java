package com.hugo.xdaily;


import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author Hugo
 *         Created on 2016/6/9 14:31.
 */
public class Rxbus {
    private static volatile Rxbus instance;
    private final Subject<Object, Object> normalBus;
    private final Subject<Object, Object> stickyBus;

    private Rxbus() {
        normalBus = new SerializedSubject<>(PublishSubject.create());
        stickyBus = new SerializedSubject<>(BehaviorSubject.create());
    }

    public static Rxbus getInstance() {
        if (instance == null) {
            synchronized (Rxbus.class) {
                if (instance == null) {
                    instance = new Rxbus();
                }
            }
        }
        return instance;
    }

    public void post(Object event) {
        normalBus.onNext(event);
    }

    public <T> Observable<T> tObservable(Class<T> eventType) {
        return normalBus.ofType(eventType);
    }

    public void postSticky(Object object) {
        stickyBus.onNext(object);
    }

    public <T> Observable<T> tObservableSticky(Class<T> eventType) {
        return stickyBus.ofType(eventType);
    }
}

