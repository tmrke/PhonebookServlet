package ru.academits.dao;

import ru.academits.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Anna on 17.06.2017.
 */
public class ContactDao {
    private List<Contact> contactList = new ArrayList<>();
    private AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact contact = new Contact();
        contact.setId(getNewId());
        contact.setFirstName("Иван");
        contact.setLastName("Иванов");
        contact.setPhone("+79123456789");
        contactList.add(contact);
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

    public void add(Contact contact) {
        contact.setId(getNewId());
        contactList.add(contact);
    }

    public void delete(Contact contact) {
        contactList.removeIf(currentContact -> currentContact.equals(contact));
    }

    public void deleteChecked(List<Contact> contacts) {
        contactList.removeAll(contacts);
    }
}
