/*
 * Copyright 2017 KudoDev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kudodev.knimble.contact;

/**
 *
 * @author IkeOTL
 */
public class ContactCache {

    private final Contact[] contacts;
    private int contactCount = 0;

    public float friction = .9f;

    public float restitution = .02f;

    public ContactCache(int maxContacts) {
        contacts = new Contact[maxContacts];
        for (int i = 0; i < maxContacts; i++) {
            contacts[i] = new Contact();
        }
    }

    public boolean hasMoreContacts() {
        return contactCount < contacts.length;
    }

    public Contact getContact() {
        if (!hasMoreContacts()) {
            return null;
        }
        return contacts[contactCount++];
    }

    public void reset() {
        contactCount = 0;
    }

    public Contact[] getContacts() {
        return contacts;
    }

    public int getContactCount() {
        return contactCount;
    }
}
