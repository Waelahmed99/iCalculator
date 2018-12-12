package idev.mysimplecalculator;

public class EquationsModel {

    int id;
    String equation;
    String date;

    public EquationsModel(int id, String equation, String date) {
        this.id = id;
        this.equation = equation;
        this.date = date;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
