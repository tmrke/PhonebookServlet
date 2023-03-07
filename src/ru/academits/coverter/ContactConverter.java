package ru.academits.coverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.academits.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactConverter {
    private final Gson gson = new Gson();
    private final JsonParser parser = new JsonParser();

    public String convertToJson(List<Contact> contactList) {
        return gson.toJson(contactList);
    }

    public List<Contact> convertFromJson(String jsonContacts) {
        List<Contact> contactList = new ArrayList<>();
        JsonElement elements = parser.parse(jsonContacts);

        for (JsonElement jsonContact : elements.getAsJsonArray()) {
            contactList.add(gson.fromJson(jsonContact, Contact.class));
        }

        return contactList;
    }
}
