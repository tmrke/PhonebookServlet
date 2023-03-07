package ru.academits.servlet;

import com.google.gson.Gson;
import ru.academits.PhoneBook;
import ru.academits.coverter.ContactConverter;
import ru.academits.coverter.ContactValidationConverter;
import ru.academits.model.Contact;
import ru.academits.service.ContactService;
import ru.academits.service.ContactValidation;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class FilterContactServlet extends HttpServlet {
    private final ContactService phoneBookService = PhoneBook.phoneBookService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;
    private final ContactValidationConverter contactValidationConverter = PhoneBook.contactValidationConverter;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (ServletOutputStream responseStream = resp.getOutputStream()) {
            String filterStringJson = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            String filterString = new StringBuilder(filterStringJson).substring(1, filterStringJson.length() - 1);

            List<Contact> contactsByFilter = phoneBookService.getContactsByFilter(filterString);
            String contactsByFilterJson = contactConverter.convertToJson(contactsByFilter);

            responseStream.write(contactsByFilterJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("error in DeleteContactServlet POST: ");
            e.printStackTrace();
        }
    }
}
