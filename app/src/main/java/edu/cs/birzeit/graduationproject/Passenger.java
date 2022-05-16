package edu.cs.birzeit.graduationproject;

public class Passenger {
    private int ID;
    private String full_name;
    private String username;
    private String password;
    private String phoneNumber;
    private int score;
    private Trip trip;

    public Passenger() {
    }

    public Passenger(int ID, String full_name, String username, String password, String phoneNumber, int score, Trip trip) {
        this.ID = ID;
        this.full_name = full_name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.score = score;
        this.trip = trip;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", score=" + score +
                ", trip=" + trip +
                '}';
    }
}
