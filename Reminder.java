import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.Scanner;
import java.time.*;
import java.io.*;

class Prescription implements Serializable {
    int hour;
    int minute;
    String name;

    Prescription(int h, int m, String n) {
        this.hour = h;
        this.minute = m;
        this.name = n;
    }

    String getName() {
        return name;
    }

    int getHour() {
        return hour;
    }

    int getMinute() {
        return minute;
    }
}

public class Reminder {

    public static void main(String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "setup":
                    setupMode();
                    break;
                case "modify":
                    modifyMode();
                    break;
                case "end":
                    endMode();
                    break;
                case "run":
                    runNotificationProcess();
                    break;
                default:
                    System.out.println("Invalid mode. Use 'setup', 'modify', 'end', or 'run'.");
            }
        } else {
            System.out.println("Invalid mode. Use 'setup', 'modify', 'end', or 'run'.");
        }
    }

    private static void setupMode() {
        // Terminate existing notification process if it exists
        endMode();

        ArrayList<Prescription> p = new ArrayList<>();
        System.out.println("Enter your prescriptions in the format 'name hour minute', enter \"done\" when done:");
        Scanner sc = new Scanner(System.in);
        String inp;
        while (!(inp = sc.nextLine()).equals("done")) {
            String[] parts = inp.split(" ");
            if (parts.length != 3) {
                System.out.println("Invalid format. Please enter in the format 'name hour minute'.");
                continue;
            }
            try {
                String name = parts[0];
                int h = Integer.parseInt(parts[1]);
                int m = Integer.parseInt(parts[2]);
                if (h < 0 || h > 23 || m < 0 || m > 59) {
                    System.out.println("Invalid time. Please enter a valid hour (0-23) and minute (0-59).");
                    continue;
                }
                p.add(new Prescription(h, m, name));
            } catch (NumberFormatException e) {
                System.out.println("Invalid time. Please enter a valid hour (0-23) and minute (0-59).");
            }
        }
        savePrescriptions(p);
        startNotificationProcess();
    }

    private static void modifyMode() {
        ArrayList<Prescription> p = loadPrescriptions();
        System.out.println("Enter your prescriptions to add in the format 'name hour minute', enter \"done\" when done:");
        Scanner sc = new Scanner(System.in);
        String inp;
        while (!(inp = sc.nextLine()).equals("done")) {
            String[] parts = inp.split(" ");
            if (parts.length != 3) {
                System.out.println("Invalid format. Please enter in the format 'name hour minute'.");
                continue;
            }
            try {
                String name = parts[0];
                int h = Integer.parseInt(parts[1]);
                int m = Integer.parseInt(parts[2]);
                if (h < 0 || h > 23 || m < 0 || m > 59) {
                    System.out.println("Invalid time. Please enter a valid hour (0-23) and minute (0-59).");
                    continue;
                }
                p.add(new Prescription(h, m, name));
            } catch (NumberFormatException e) {
                System.out.println("Invalid time. Please enter a valid hour (0-23) and minute (0-59).");
            }
        }
        savePrescriptions(p);
        notifyUpdate();
    }

    private static void endMode() {
        File file = new File("notification_process.pid");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String pid = reader.readLine();
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    new ProcessBuilder("taskkill", "/F", "/PID", pid).start();
                } else {
                    new ProcessBuilder("kill", "-9", pid).start(); // Send SIGKILL (signal 9)
                }
                file.delete();
            } catch (IOException e) {
                System.out.println("Error ending notification process: " + e.getMessage());
            }
        } else {
            System.out.println("No notification process found.");
        }

        // Clean up data files
        File prescriptionsFile = new File("prescriptions.dat");
        if (prescriptionsFile.exists()) {
            prescriptionsFile.delete();
        }

        File updateFlagFile = new File("update_flag.txt");
        if (updateFlagFile.exists()) {
            updateFlagFile.delete();
        }

        File pidFile = new File("notification_process.pid");
        if (pidFile.exists()) {
            pidFile.delete();
        }
    }

    private static void savePrescriptions(ArrayList<Prescription> prescriptions) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("prescriptions.dat"))) {
            oos.writeObject(prescriptions);
        } catch (IOException e) {
            System.out.println("Error saving prescriptions: " + e.getMessage());
        }
    }

    private static ArrayList<Prescription> loadPrescriptions() {
        File file = new File("prescriptions.dat");
        if (!file.exists()) {
            System.out.println("No prescriptions found. Please run in setup mode first.");
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                ArrayList<?> list = (ArrayList<?>) obj;
                if (list.size() > 0 && list.get(0) instanceof Prescription) {
                    return (ArrayList<Prescription>) list;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading prescriptions: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private static void startNotificationProcess() {
        try {
            Process process = new ProcessBuilder("java", "Reminder", "run").start();
            try (PrintWriter writer = new PrintWriter(new FileWriter("notification_process.pid"))) {
                writer.println(process.pid());
            }
        } catch (IOException e) {
            System.out.println("Error starting notification process: " + e.getMessage());
        }
    }

    private static void notifyUpdate() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("update_flag.txt"))) {
            writer.println("update");
        } catch (IOException e) {
            System.out.println("Error notifying update: " + e.getMessage());
        }
    }

    private static void runNotificationProcess() {
        ArrayList<Prescription> p = loadPrescriptions();
        if (p.isEmpty()) {
            System.out.println("No prescriptions to schedule.");
            return;
        }
        // Ensure that the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported on this platform");
            return;
        }

        // Create a system tray instance
        SystemTray systemTray = SystemTray.getSystemTray();
        TrayIcon trayIcon = createTrayIcon();

        // Add the tray icon to the system tray
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Error adding tray icon: " + e.getMessage());
            return;
        }

        // Create a scheduled task to send notifications at specific times
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule the task for each prescription
        for (int i = 0; i < p.size(); ++i) {
            Prescription pr = p.get(i);
            scheduleDailyNotificationAtTime(pr, trayIcon, scheduler);
        }

        // Periodically check for updates
        scheduler.scheduleAtFixedRate(() -> {
            File updateFile = new File("update_flag.txt");
            if (updateFile.exists()) {
                updateFile.delete();
                ArrayList<Prescription> updatedPrescriptions = loadPrescriptions();
                // Reschedule notifications with updated prescriptions
                scheduler.shutdownNow();
                runNotificationProcess();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private static TrayIcon createTrayIcon() {
        // Set the image for the tray icon
        ImageIcon icon = new ImageIcon("path_to_your_icon.png");  // Use your own icon file
        Image image = icon.getImage();
        
        // Create a TrayIcon with an optional popup menu
        PopupMenu popupMenu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popupMenu.add(exitItem);

        TrayIcon trayIcon = new TrayIcon(image, "Medicine Reminder", popupMenu);
        trayIcon.setImageAutoSize(true); // Make the icon size adjust automatically
        return trayIcon;
    }

    private static void scheduleDailyNotificationAtTime(Prescription prescription, TrayIcon trayIcon, ScheduledExecutorService scheduler) {
        // Get the current time
        LocalTime now = LocalTime.now();
        LocalTime targetTime = LocalTime.of(prescription.getHour(), prescription.getMinute());

        // Calculate the delay until the target time
        long initialDelay = Duration.between(now, targetTime).toMillis();
        if (initialDelay < 0) {
            // If the target time is earlier today, schedule it for tomorrow
            initialDelay += Duration.ofDays(1).toMillis();
        }

        System.out.println("Scheduling notification for " + prescription.getName() + " at " + targetTime);

        // Schedule the daily notification task
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Displaying notification for " + prescription.getName());
            // Display the prescription name in the notification
            trayIcon.displayMessage("Reminder: " + prescription.getName(), 
                                    "Time to take your medication!", 
                                    TrayIcon.MessageType.INFO);
        }, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS); // Repeat every 24 hours
    }
}
