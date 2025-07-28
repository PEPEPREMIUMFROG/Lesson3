package Lesson14.phonebook;

public class Record {
    private final String name;
    private final String phone;

    public Record(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Record{name='" + name + "', phone='" + phone + "'}";
    }
}