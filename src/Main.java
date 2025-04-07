import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerHour;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerHour) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerHour = basePricePerHour;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays, int rentalHours) {
        return basePricePerHour * (rentalDays * 24 + rentalHours);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;
    private int hours;

    public Rental(Car car, Customer customer, int days, int hours) {
        this.car = car;
        this.customer = customer;
        this.days = days;
        this.hours = hours;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days, int hours) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days, hours));
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();

                System.out.println("\nAvailable Cars:");
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine();

                System.out.print("Enter the number of **days** for rental: ");
                int rentalDays = scanner.nextInt();

                System.out.print("Enter the number of **additional hours**: ");
                int rentalHours = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays, rentalHours);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Duration: " + rentalDays + " day(s) and " + rentalHours + " hour(s)");
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, rentalDays, rentalHours);
                        System.out.println("\nCar rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();

                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && !car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getCar() == carToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
        rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 90.0));
        rentalSystem.addCar(new Car("C004", "Ford", "Fiesta", 55.0));
        rentalSystem.addCar(new Car("C005", "Hyundai", "Verna", 60.0));
        rentalSystem.addCar(new Car("C006", "Tata", "Harrier", 80.0));
        rentalSystem.addCar(new Car("C007", "Kia", "Seltos", 75.0));
        rentalSystem.addCar(new Car("C008", "Nissan", "Magnite", 65.0));
        rentalSystem.addCar(new Car("C009", "Renault", "Kiger", 68.0));
        rentalSystem.addCar(new Car("C010", "Suzuki", "Swift", 59.0));
        rentalSystem.addCar(new Car("C011", "Jeep", "Compass", 95.0));
        rentalSystem.addCar(new Car("C012", "MG", "Hector", 85.0));
        rentalSystem.addCar(new Car("C013", "Skoda", "Rapid", 64.0));
        rentalSystem.addCar(new Car("C014", "Volkswagen", "Vento", 66.0));
        rentalSystem.addCar(new Car("C015", "Audi", "A4", 120.0));
        rentalSystem.addCar(new Car("C016", "BMW", "X1", 140.0));
        rentalSystem.addCar(new Car("C017", "Mercedes", "C-Class", 150.0));
        rentalSystem.addCar(new Car("C018", "Volvo", "XC40", 135.0));
        rentalSystem.addCar(new Car("C019", "Lexus", "ES", 145.0));
        rentalSystem.addCar(new Car("C020", "Tesla", "Model 3", 160.0));
        rentalSystem.addCar(new Car("C021", "Toyota", "Innova", 95.0));
        rentalSystem.addCar(new Car("C022", "Hyundai", "i20", 70.0));
        rentalSystem.addCar(new Car("C023", "Tata", "Nexon", 85.0));
        rentalSystem.addCar(new Car("C024", "Kia", "Carens", 88.0));
        rentalSystem.addCar(new Car("C025", "Honda", "City", 83.0));
        rentalSystem.addCar(new Car("C026", "Ford", "EcoSport", 78.0));
        rentalSystem.addCar(new Car("C027", "Nissan", "Sunny", 79.0));
        rentalSystem.addCar(new Car("C028", "Renault", "Duster", 82.0));
        rentalSystem.addCar(new Car("C029", "Suzuki", "Baleno", 65.0));
        rentalSystem.addCar(new Car("C030", "Jeep", "Meridian", 99.0));
        rentalSystem.addCar(new Car("C031", "MG", "Astor", 84.0));
        rentalSystem.addCar(new Car("C032", "Skoda", "Kushaq", 74.0));
        rentalSystem.addCar(new Car("C033", "Volkswagen", "Taigun", 73.0));
        rentalSystem.addCar(new Car("C034", "Audi", "Q2", 122.0));
        rentalSystem.addCar(new Car("C035", "BMW", "3 Series", 148.0));
        rentalSystem.addCar(new Car("C036", "Mercedes", "GLA", 155.0));
        rentalSystem.addCar(new Car("C037", "Volvo", "S60", 137.0));
        rentalSystem.addCar(new Car("C038", "Lexus", "NX", 150.0));
        rentalSystem.addCar(new Car("C039", "Tesla", "Model Y", 165.0));
        rentalSystem.addCar(new Car("C040", "Toyota", "Fortuner", 108.0));
        rentalSystem.addCar(new Car("C041", "Hyundai", "Creta", 89.0));
        rentalSystem.addCar(new Car("C042", "Tata", "Punch", 72.0));
        rentalSystem.addCar(new Car("C043", "Kia", "Sonet", 76.0));
        rentalSystem.addCar(new Car("C044", "Honda", "WR-V", 80.0));
        rentalSystem.addCar(new Car("C045", "Ford", "Figo", 69.0));
        rentalSystem.addCar(new Car("C046", "Nissan", "Terrano", 87.0));
        rentalSystem.addCar(new Car("C047", "Renault", "Triber", 70.0));
        rentalSystem.addCar(new Car("C048", "Suzuki", "Dzire", 66.0));
        rentalSystem.addCar(new Car("C049", "Jeep", "Wrangler", 115.0));
        rentalSystem.addCar(new Car("C050", "MG", "Gloster", 99.0));

        rentalSystem.menu();
    }
}
