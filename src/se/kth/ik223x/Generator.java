package se.kth.ik223x;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Generator {

    public static void main(String[] args) {
        try {
            System.out.println("Usage: -recordcount numberOfRecords -path filePath -filecount numberOfFiles\n");

            int numberOfRecords = 0, numberOfFiles = 0;
            String filePath = null;

            if (args == null || args.length != 6)  {
                errorMessage();
            }

            if (args[0].startsWith("-") && args[0].equals("-recordcount")) {
                try {
                    numberOfRecords = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    errorMessage();
                }
            } else {
                errorMessage();
            }

            if (args[2].startsWith("-") && args[2].equals("-path")) {
                filePath = args[3];
            } else {
                errorMessage();
            }

            if (args[3].startsWith("-") && args[3].equals("-path")) {
                try {
                    numberOfFiles = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    errorMessage();
                }
            } else {
                errorMessage();
            }

            Generator generator = new Generator(numberOfRecords, numberOfFiles, filePath);
            generator.startDataGeneration();
        } catch (IOException ex) {
            System.err.println("=== Error occurred during generating test data! ===");
            ex.printStackTrace();

            errorMessage();
        }
    }

    private static void errorMessage() {
        System.out.println("\nPlease provide correct parameters.");
        System.out.println("Usage: -recordcount numberOfRecords -path filePath -filecount numberOfFiles\n");

        System.exit(1);
    }

    private String filePath;
    private int numberOfFiles;
    private int numberOfRecords;

    public Generator(int numberOfRecords, int numberOfFiles, String filePath) {
        this.numberOfRecords = numberOfRecords;
        this.numberOfFiles = numberOfFiles;
        this.filePath = filePath;
    }

    /**
     * Base method for generating files based on parameters in the constructor
     *
     * @throws IOException
     */
    public void startDataGeneration() throws IOException {
        int rowCount = numberOfRecords / numberOfFiles;
        for (int i = 1; i <= numberOfFiles; i++) {
            generateData(i, rowCount);
        }
    }

    /**
     * Generates files based on given parameters
     *
     * @param counter - file number, in general there will be bunch of files separated by a number
     * @param rowCount - the number of rows in each file
     * @throws IOException
     */
    private void generateData(int counter, int rowCount) throws IOException {
        int totalRows = 0;

        BufferedWriter medRecsWriter = getBufferedWriter("medical_records_" + counter + ".dat");
        BufferedWriter userWriter = getBufferedWriter("user_" + counter + ".dat");

        while (totalRows < rowCount) {
            StringBuilder medicalRecords = new StringBuilder();
            generateId(medicalRecords);
            generateTimestamp(medicalRecords);
            generateVitalSigns(medicalRecords);

            medRecsWriter.append(medicalRecords);
            medRecsWriter.newLine();

            StringBuilder user = new StringBuilder();
            generateId(user);
            generateUser(user);

            userWriter.append(user);
            userWriter.newLine();

            totalRows++;
        }

        medRecsWriter.flush();
        medRecsWriter.close();

        userWriter.flush();
        userWriter.close();
    }

    /**
     * Generates id numbers between 1 and 100,000,000.
     */
    private void generateId(StringBuilder builder) {
        int min = 1, max = 100000000;
        Random random = new Random();
        int randomId = random.nextInt(max - min + 1) + min;

        builder.append("id:").append(randomId).append(",");
    }

    /**
     * Generates random string
     *
     * @return - returns generated random string
     */
    private String generateString(Random random, int length, String characters) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

    /**
     * Generates random timestamp in milliseconds between
     * last year of today and today. For instance,
     * if today is 10-May-2013, returned timestamp is
     * a date between 10-May-2012 and 10-May-2013 in milliseconds.
     */
    private void generateTimestamp(StringBuilder medicalRecords) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, Calendar.JANUARY, 1, 0, 0, 0);// Starting from January 01, 2013 00:00:00
        long dateInMillis = calendar.getTimeInMillis();

        int sixMonthInSeconds = 6 * 30 * 24 * 60 * 60;
        int minDateInSeconds = 0;

        Random randomDateInLastSixMonths = new Random();
        int random = randomDateInLastSixMonths.nextInt(sixMonthInSeconds - minDateInSeconds + 1) + minDateInSeconds;
        long randomInMillis = random * 1000L;

        medicalRecords.append("timestamp:").append(dateInMillis + randomInMillis).append(",");
    }

    /**
     * Generates an ordinary user with random strings.
     *
     * @param user - all generated data is appended following string builder
     */
    private void generateUser(StringBuilder user) {
        Random random = new Random();
        int length = 10; // This length will be applied for all strings
        String characters = "0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz_";

        user.append("first_name:").append(generateString(random, length, characters)).append(",");
        user.append("last_name:").append(generateString(random, length, characters)).append(",");
        user.append("username:").append(generateString(random, length, characters)).append(",");
        user.append("password:").append(generateString(random, length, characters)).append(",");
        user.append("email:").append(generateString(random, length, characters)).append(",");
        user.append("role:").append(generateString(random, length, characters)).append(",");
        user.append("cell_phone:").append(generateString(random, length, characters)).append(",");
        user.append("country:").append(generateString(random, length, characters)).append(",");
        user.append("county:").append(generateString(random, length, characters)).append(",");
        user.append("city:").append(generateString(random, length, characters)).append(",");
        user.append("street:").append(generateString(random, length, characters));
    }

    /**
     * Generates vital signs based on medical standards.
     *
     * @param medicalRecords - all vital signs are written in string builder.
     */
    private void generateVitalSigns(StringBuilder medicalRecords) {
        Random vitalSignGenerator = new Random();

        int minPulseRate = 50, maxPulseRate = 300;
        int pulseRate = vitalSignGenerator.nextInt(maxPulseRate - minPulseRate + 1) + minPulseRate;
        medicalRecords.append("pulse_rate:").append(pulseRate).append(",");

        int minSpO2 = 75, maxSpO2 = 99;
        int spO2 = vitalSignGenerator.nextInt(maxSpO2 - minSpO2 + 1) + minSpO2;
        medicalRecords.append("spo2:").append(spO2).append(",");

        int minTemperature = 339, maxTemperature = 396;
        float temperature = (vitalSignGenerator.nextInt(maxTemperature - minTemperature) + minTemperature) / 10f;
        medicalRecords.append("body_temperature:").append(temperature).append(",");

        int minBloodPressure = 50, maxBloodPressure = 230;
        int bloodPressure = vitalSignGenerator.nextInt(maxBloodPressure - minBloodPressure + 1) + minBloodPressure;
        medicalRecords.append("blood_pressure:").append(bloodPressure).append(",");

        int minRespirationRate = 12, maxRespirationRate = 60;
        int respirationRate = vitalSignGenerator.nextInt(maxRespirationRate - minRespirationRate + 1) + minRespirationRate;
        medicalRecords.append("respiration_rate:").append(respirationRate).append(",");

        int minBloodGlucose = 60, maxBloodGlucose = 180;
        int bloodGlucose = vitalSignGenerator.nextInt(maxBloodGlucose - minBloodGlucose + 1) + minBloodGlucose;
        medicalRecords.append("blood_glucose:").append(bloodGlucose).append(",");

        int minVitalCapacity = 25, maxVitalCapacity = 65;
        float vitalCapacity = (vitalSignGenerator.nextInt(maxVitalCapacity - minVitalCapacity + 1) + minVitalCapacity) / 10f;
        medicalRecords.append("vital_capacity:").append(vitalCapacity).append(",");

        int minFef = 25, maxFef = 75;// Forced expiratory flow (FEF)
        int fef = vitalSignGenerator.nextInt(maxFef - minFef + 1) + minFef;
        medicalRecords.append("forced_expiratory_flow:").append(fef).append(",");

        int minFif = 25, maxFif = 75;// Forced inspiratory flow (FIF)
        int fif = vitalSignGenerator.nextInt(maxFif - minFif + 1) + minFif;
        medicalRecords.append("forced_inspiratory_flow:").append(fif).append(",");

        int minTidalVolume = 200, maxTidalVolume = 800;
        int tidalVolume = vitalSignGenerator.nextInt(maxTidalVolume - minTidalVolume + 1) + minTidalVolume;
        medicalRecords.append("tidal_volume:").append(tidalVolume).append(",");

        int minEndTidalCO2 = 25, maxEndTidalCO2 = 65;
        int endTidalCO2 = vitalSignGenerator.nextInt(maxEndTidalCO2 - minEndTidalCO2 + 1) + minEndTidalCO2;
        medicalRecords.append("end_tidal_co2:").append(endTidalCO2).append(",");

        int minGaitSpeed = 4, maxGaitSpeed = 19;
        float gaitSpeed = (vitalSignGenerator.nextInt(maxGaitSpeed - minGaitSpeed + 1) + minGaitSpeed) / 10f;
        medicalRecords.append("gait_speed:").append(gaitSpeed);
    }

    /**
     * Returns a buffered writer based on a given file name.
     *
     * @param fileName - name of the file
     * @return - returns buffered writer
     * @throws IOException
     */
    private BufferedWriter getBufferedWriter(String fileName) throws IOException {
        File file = new File(filePath + File.separator + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        return new BufferedWriter(fileWriter);
    }
}
