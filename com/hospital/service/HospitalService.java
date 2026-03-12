package com.hospital.service;

import com.hospital.patient.Patient;
import com.hospital.exception.DuplicatePatientException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.InvalidAgeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalService {

    private static final String FILE_NAME = "patients.txt";

    // Validate age before adding
    private void validateAge(int age) throws InvalidAgeException {
        if (age < 0 || age > 120) {
            throw new InvalidAgeException("Invalid age: " + age + ". Age must be between 0 and 120.");
        }
    }

    // Check for critical patient alert
    private void checkCriticalPatient(Patient p) {
        if (p.getAge() > 60 && p.getDisease().equalsIgnoreCase("Heart Problem")) {
            System.out.println("⚠ Priority Patient – Immediate Attention Required");
        }
    }

    // Load all patients from file
    private List<Patient> loadPatientsFromFile() {
        List<Patient> patients = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return patients;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String disease = parts[3].trim();
                    patients.add(new Patient(id, name, age, disease));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return patients;
    }

    // Add patient to file
    public void addPatient(Patient p) throws DuplicatePatientException, InvalidAgeException {
        // Validate age
        validateAge(p.getAge());

        // Check for duplicate
        List<Patient> existingPatients = loadPatientsFromFile();
        for (Patient existing : existingPatients) {
            if (existing.getPatientId() == p.getPatientId()) {
                throw new DuplicatePatientException("Patient with ID " + p.getPatientId() + " already exists!");
            }
        }

        // Write to file using FileWriter and BufferedWriter (append mode)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(p.getPatientId() + "," + p.getPatientName() + "," + p.getAge() + "," + p.getDisease());
            bw.newLine();
            System.out.println("Patient added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        // Check for critical patient alert
        checkCriticalPatient(p);
    }

    // Search patient by ID
    public Patient searchPatient(int patientId) throws PatientNotFoundException {
        List<Patient> patients = loadPatientsFromFile();

        for (Patient p : patients) {
            if (p.getPatientId() == patientId) {
                return p;
            }
        }

        throw new PatientNotFoundException("Patient with ID " + patientId + " not found!");
    }

    // Display all patients
    public void displayPatients() {
        List<Patient> patients = loadPatientsFromFile();

        if (patients.isEmpty()) {
            System.out.println("No patient records found.");
            return;
        }

        System.out.println("========= All Patient Records =========");
        for (Patient p : patients) {
            p.displayPatient();
        }
    }
}
