package ru.academits.service;

import ru.academits.PhoneBook;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;

import java.util.List;

public class ContactService {
    private final ContactDao contactDao = PhoneBook.contactDao;

    private boolean isExistContactWithPhone(String phone) {
        List<Contact> contactList = contactDao.getAllContacts();

        for (Contact contact : contactList) {
            if (contact.getPhone().equals(phone)) {
                return true;
            }
        }

        return false;
    }

    public ContactValidation validateContact(List<Contact> contactList, boolean forDeletable) {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);
        String regex = "^(\\+7|7|8)?[\\s\\-]?\\(?[0-9]{3}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$";

        for (Contact contact : contactList) {
            if (contact.getFirstName().isEmpty()) {
                contactValidation.setValid(false);
                contactValidation.setError("Поле Имя должно быть заполнено.");

                return contactValidation;
            }

            if (contact.getLastName().isEmpty()) {
                contactValidation.setValid(false);
                contactValidation.setError("Поле Фамилия должно быть заполнено.");

                return contactValidation;
            }

            if (!contact.getPhone().matches(regex)) {
                contactValidation.setValid(false);
                contactValidation.setError("Поле Телефон должно быть заполнено в формате +79995551234.");

                return contactValidation;
            }

            if (contact.getPhone().isEmpty()) {
                contactValidation.setValid(false);
                contactValidation.setError("Поле Телефон должно быть заполнено.");

                return contactValidation;
            }

            if (!forDeletable && isExistContactWithPhone(contact.getPhone())) {
                contactValidation.setValid(false);
                contactValidation.setError("Номер телефона не должен дублировать другие номера в телефонной книге.");

                return contactValidation;
            }
        }

        return contactValidation;
    }

    public ContactValidation addContact(List<Contact> contactList) {
        if (contactList.size() > 1) {
            throw new IndexOutOfBoundsException("Количество добавляемых контактов не может быть больше одного.");
        }

        ContactValidation contactValidation = validateContact(contactList, false);

        if (contactValidation.isValid()) {
            contactDao.add(contactList.get(0));
        }

        return contactValidation;
    }

    public ContactValidation deleteContact(List<Contact> contactList) {
        ContactValidation contactValidation = validateContact(contactList, true);

        for (Contact contact : contactList) {
            if (contactValidation.isValid()) {
                contactDao.delete(contact);
            }
        }

        return contactValidation;
    }

    public List<Contact> getContactsByFilter(String filterString) {
        return contactDao.getContactsByFilter(filterString);
    }

    public List<Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }
}
