/**
 * Copyright 2016 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package io.reactivex.internal.operators.flowable;

import java.util.*;

import org.reactivestreams.*;

import io.reactivex.Flowable;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;

public final class FlowableIgnoreElements<T> extends Flowable<T> {

    final Publisher<T> source;
    
    public FlowableIgnoreElements(Publisher<T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(final Subscriber<? super T> t) {
        source.subscribe(new IgnoreElementsSubscriber<T>(t));
    }
    
    static final class IgnoreElementsSubscriber<T> implements QueueSubscription<T>, Subscriber<T> {
        final Subscriber<? super T> actual;

        Subscription s;
        
        public IgnoreElementsSubscriber(Subscriber<? super T> actual) {
            this.actual = actual;
        }

        @Override
        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.s, s)) {
                this.s = s;
                actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }
        
        @Override
        public void onNext(T t) {
            // deliberately ignored
        }
        
        @Override
        public void onError(Throwable t) {
            actual.onError(t);
        }
        
        @Override
        public void onComplete() {
            actual.onComplete();
        }
        
        @Override
        public boolean add(T e) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public boolean offer(T e) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public T remove() {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public T poll() {
            return null; // empty, always
        }

        @Override
        public T element() {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public T peek() {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean contains(Object o) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public Iterator<T> iterator() {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public <U> U[] toArray(U[] a) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        @Override
        public void clear() {
            // always empty
        }

        @Override
        public void request(long n) {
            // never emits a value
        }

        @Override
        public void cancel() {
            s.cancel();
        }

        @Override
        public int requestFusion(int mode) {
            return mode & ASYNC;
        }
    }
}