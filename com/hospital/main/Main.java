package com.hospital.main;

import com.hospital.patient.Patient;
import com.hospital.service.HospitalService;
import com.hospital.exception.InvalidAgeException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.DuplicatePatientException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HospitalService service = new HospitalService();

        // Display existing records from file on startup
        System.out.println("--- Records loaded from patients.txt on startup ---");
        service.displayPatients();
        System.out.println();

        boolean running = true;

        while (running) {
            System.out.println("===== Hospital Patient Record System =====");
            System.out.println("1. Add Patient");
            System.out.println("2. Display All Patients");
            System.out.println("3. Search Patient by ID");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter Patient ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Patient Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Age: ");
                        int age = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Disease: ");
                        String disease = sc.nextLine();

                        Patient p = new Patient(id, name, age, disease);
                        service.addPatient(p);

                    } catch (InvalidAgeException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (DuplicatePatientException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    service.displayPatients();
                    break;

                case 3:
                    try {
                        System.out.print("Enter Patient ID to search: ");
                        int searchId = sc.nextInt();
                        sc.nextLine();

                        Patient found = service.searchPatient(searchId);
                        System.out.println("Patient Found:");
                        found.displayPatient();

                    } catch (PatientNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    running = false;
                    System.out.println("Exiting... Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            System.out.println();
        }

        sc.close();
    }
}
