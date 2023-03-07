function Contact(firstName, lastName, phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.checked = false;
    this.shown = true;
}

new Vue({
    el: "#app",
    data: {
        validation: false,
        serverValidation: false,
        isMainCheckboxChecked: false,
        firstName: "",
        lastName: "",
        phone: "",
        rows: [],
        filterString: "",
        serverError: "",
        regex: /^(\+7|7|8)?[\s\-]?\(?[0-9]{3}\)?[\s\-]?[0-9]{3}[\s\-]?[0-9]{2}[\s\-]?[0-9]{2}$/
    },
    methods: {
        contactToString: function (contact) {
            var note = "(";
            note += contact.firstName + ", ";
            note += contact.lastName + ", ";
            note += contact.phone;
            note += ")";
            return note;
        },
        convertContactList: function (contactListFromServer) {
            return contactListFromServer.map(function (contact, i) {
                return {
                    id: contact.id,
                    firstName: contact.firstName,
                    lastName: contact.lastName,
                    phone: contact.phone,
                    checked: false,
                    shown: true,
                    number: i + 1
                };
            });
        },
        addContact: function () {
            if (this.hasError) {
                this.validation = true;
                this.serverValidation = false;
                return;
            }

            var self = this;

            var contact = [new Contact(this.firstName, this.lastName, this.phone)];
            $.ajax({
                type: "POST",
                url: "/phonebook/add",
                data: JSON.stringify(contact)
            }).done(function () {
                self.serverValidation = false;
            }).fail(function (ajaxRequest) {
                var contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            }).always(function () {
                self.loadData();
            });

            self.firstName = "";
            self.lastName = "";
            self.phone = "";
            self.validation = false;
        },
        deleteContact: function (id) {
            var contact = [new Contact(this.rows[id].firstName, this.rows[id].lastName, this.rows[id].phone)];
            var self = this;

            $.ajax({
                type: "POST",
                url: "/phonebook/delete",
                data: JSON.stringify(contact)
            }).done(function () {
                self.serverValidation = false;
            }).always(function () {
                self.loadData();
            });
        },
        deleteCheckedContacts: function () {
            var selectedRows = this.rows.filter(function (row) {
                return row.checked === true;
            });

            var selectedContacts = [];
            var self = this;

            selectedRows.forEach(function (row) {
                selectedContacts.push(new Contact(row.firstName, row.lastName, row.phone));
            })

            $.ajax({
                type: "POST",
                url: "/phonebook/delete",
                data: JSON.stringify(selectedContacts)
            }).done(function () {
                self.serverValidation = false;
            }).always(function () {
                self.loadData();
            });
        },
        selectAllCheckboxes: function () {
            var isChecked = this.isMainCheckboxChecked;

            this.rows.forEach(function (row) {
                row.checked = !isChecked;
            });
        },
        filter: function () {
            var filterString = this.filterString.toLowerCase();

            var self = this;

            $.ajax({
                type: "POST",
                url: "/phonebook/filter",
                data: JSON.stringify(filterString)
            }).done(function (response) {
                self.serverValidation = false;
                var contactListFromServer = JSON.parse(response);
                self.rows = self.convertContactList(contactListFromServer)
            }).fail(function (ajaxRequest) {
                var contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            });
        },
        dropFilterString: function () {
            this.filterString = "";
            this.loadData();
        },
        loadData: function () {
            var self = this;

            $.get("/phonebook/get/all").done(function (response) {
                var contactListFormServer = JSON.parse(response);
                self.rows = self.convertContactList(contactListFormServer);
            });
        }
    },
    computed: {
        firstNameError: function () {
            if (this.firstName) {
                return {
                    message: "",
                    error: false
                };
            }

            return {
                message: "Поле Имя должно быть заполнено.",
                error: true
            };
        },
        lastNameError: function () {
            if (!this.lastName) {
                return {
                    message: "Поле Фамилия должно быть заполнено.",
                    error: true
                };
            }

            return {
                message: "",
                error: false
            };
        },
        phoneError: function () {
            if (!this.phone) {
                return {
                    message: "Поле Телефон должно быть заполнено.",
                    error: true
                };
            }

            if (!this.regex.test(this.phone)) {
                return {
                    message: "Поле Телефон должно быть заполнено в формате +79995551234.",
                    error: true
                };
            }

            var self = this;

            var sameContact = this.rows.some(function (c) {
                return c.phone === self.phone;
            });

            if (sameContact) {
                return {
                    message: "Номер телефона не должен дублировать другие номера в телефонной книге.",
                    error: true
                };
            }

            return {
                message: "",
                error: false
            };
        },
        hasError: function () {
            return this.lastNameError.error || this.firstNameError.error || this.phoneError.error;
        }
    },
    created: function () {
        this.loadData();
    }
});

