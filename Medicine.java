import java.util.Scanner;

public class Medicine{
    private String name;
    private String function; //what the medicine does
    private String expirationDate;
    private int AmountPerInterval; //the amount of this medicine taken per notification
    private int TimesPerDay; //the amount of times per day medicine needs to be taken
    private String[]times; //the list of times stored in a string

    public Medicine(String n, String f, String ed, int api, int tpd, Scanner input){
        name = n;
        function = f;
        expirationDate = ed;
        AmountPerInterval = api;
        TimesPerDay = tpd;
        this.times = new String[this.TimesPerDay];
    }

    public String GetName(){
    	return name;
    }

    public void SetName(String name){
    	this.name = name;
    }

    public String GetFunction(){
    	return function;
    }

    public void SetFunction(String function){
    	this.function = function;
    }

    public String GetExpirationDate(){
    	return expirationDate;
    }

    public void SetExpirationDate(String ed){
    	expirationDate = ed;
    }

    public void SetExpirationDate(int month, int day, int year){
    	expirationDate = month + "/" + day + "/" + year;
    }

    public void SetExpirationDate(Scanner input){
    	System.out.println("Please enter the month, day and year of this medication's expiration, delimeted by a space. \n ex)12/2/2029");
    	expirationDate = input.nextInt() + "/" + input.nextInt() + "/" + input.nextInt();
    }

    public int GetAmountPerInterval(){
    	return AmountPerInterval;
    }

    public void SetAmountPerInterval(int amount){
    	AmountPerInterval = amount;
    }

    public int GetTimesPerDay(){
    	return TimesPerDay;
    }

    public void SetTimesPerDay(int times, Scanner input){
    	TimesPerDay = times;
    	this.times = new String[this.TimesPerDay];
    	System.out.print("Please re-enter the times you will take the medicine");
    }

    public void SetTimes(Scanner input){
        System.out.println("Please enter the times you will take the medicine, delimited by a space. \n ex) 3:00 12:00 16:00");
        for(int i = 0; i < TimesPerDay; i++){
            times[i] = input.next();
        }
    }

    public void PrintInfo(){
    	System.out.println("Medication name: " + name);
    	System.out.println("Description: " + function);
    	System.out.println("Expiration Date: " + expirationDate);
    	System.out.println("Times taken per day: " + AmountPerInterval * TimesPerDay);
    	System.out.println("When to take: ");
    	for(int i = 0; i < times.length; i++){
    		System.out.print(" | " + times[i] + " | ");
    	}
    	System.out.println();
    	System.out.println("Amount to take per period of time: " + AmountPerInterval);
    }
}