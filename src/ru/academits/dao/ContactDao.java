package ru.academits.dao;

import ru.academits.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Anna on 17.06.2017.
 */
public class ContactDao {
    private final List<Contact> contactList = new ArrayList<>();
    private final AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact contact1 = new Contact();
        contact1.setId(getNewId());
        contact1.setFirstName("Иван");
        contact1.setLastName("Иванов");
        contact1.setPhone("+79123456789");
        contactList.add(contact1);

        Contact contact2 = new Contact();
        contact2.setId(getNewId());
        contact2.setFirstName("Марина");
        contact2.setLastName("Маринина");
        contact2.setPhone("+79999999999");
        contactList.add(contact2);

        Contact contact3 = new Contact();
        contact3.setId(getNewId());
        contact3.setFirstName("Марина");
        contact3.setLastName("Маринина");
        contact3.setPhone("+79999999991");
        contactList.add(contact3);
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
}
