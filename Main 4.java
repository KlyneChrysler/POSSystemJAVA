import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

    public static Scanner scan;
    public static CustomerCRUD customerManager;

    public static void main(String[] args) {     
        customerManager = new CustomerCRUD();
        scan = new Scanner(System.in);
        String customer_name = "", invoice_item = "";
        int choice = 0, customer_id = 0, invoice_id = 0, invoice_qty = 0;
        double invoice_price = 0;
        boolean flag = true;

        while(flag){
            choice = 0;
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Welcome to POS System");
            System.out.println("[1] Create Customer");
            System.out.println("[2] Retrieve Customer");
            System.out.println("[3] Delete Customer");
            System.out.println("[4] Exit");
            System.out.print("Enter number of choice: ");
            try{
                choice = Integer.parseInt(scan.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("=============================================");
                        System.out.println("---------------Create Customer---------------");
                        System.out.print("Enter Customer ID: ");
                        customer_id = Integer.parseInt(scan.nextLine());
                        System.out.print("Enter Customer Name: ");
                        customer_name = scan.nextLine();
                        if(customer_name.isBlank()){
                            throw new ErrorHandler("Name must not be empty. Thank you!");
                        }
                        createCustomer(customer_id, customer_name);
                        System.out.println("---------------------------------------------");
                        break;

                    case 2:
                        System.out.println("=============================================");
                        System.out.println("--------------Retrieve Customer--------------");
                        displayCustomers();
                        System.out.print("Enter Customer ID: ");
                        customer_id = Integer.parseInt(scan.nextLine());
                        //if(retrieveCustomer(customer_id) == null){
                          //throw new ErrorHandler("Customer with ID " + customer_id + " does not exits!");
                        //}
                        retrieveCustomer(customer_id);
                        displayInvoices(customer_id);
                        System.out.println("[1] Create Invoice");
                        System.out.println("[2] Update Invoice");
                        System.out.println("[3] Exit");
                        System.out.print("Enter number of choice: ");
                        choice = Integer.parseInt(scan.nextLine());
                        switch (choice) {
                            case 1:
                                System.out.println("=============================================");
                                System.out.println("---------------Create Invoice----------------");
                                System.out.print("Enter Invoice ID: ");
                                invoice_id = Integer.parseInt(scan.nextLine());
                                System.out.print("Enter Item: ");
                                invoice_item = scan.nextLine();
                                if(invoice_item.isBlank()){
                                    throw new ErrorHandler("Item must not be empty. Thank you!");
                                }
                                System.out.print("Enter Quantity: ");
                                invoice_qty = Integer.parseInt(scan.nextLine());
                                System.out.print("Enter Price: ");
                                invoice_price = Double.parseDouble(scan.nextLine());
                                createInvoice(customer_id, invoice_id, invoice_item, invoice_qty, invoice_price);
                                System.out.println("---------------------------------------------");
                                break;
                        
                            case 2:
                                System.out.println("=============================================");
                                System.out.println("---------------Update Invoice----------------");
                                System.out.print("Enter Invoice ID: ");
                                invoice_id = Integer.parseInt(scan.nextLine());
                                Invoice record = retrieveInvoice(customer_id, invoice_id);
                                if(record == null){
                                     throw new ErrorHandler("Invoice with ID " + invoice_id + " does not exist!");
                                }
                                System.out.println("---------------------------------------------");
                                System.out.print("Enter Updated Price: ");
                                invoice_price = Double.parseDouble(scan.nextLine());
                                updateInvoice(customer_id, invoice_id, invoice_price);
                                System.out.println("---------------------------------------------");
                                break;

                            case 3:
                                flag = false;
                                break;

                            default:
                                throw new ErrorHandler("Please input numbers from 1-2 only. Thank you!");
                        }
                        break;

                    case 3:
                        System.out.println("=============================================");
                        System.out.println("---------------Delete Customer---------------");
                        displayCustomers();
                        System.out.print("Enter Customer ID: ");
                        customer_id = Integer.parseInt(scan.nextLine());
                        deleteCustomer(customer_id);
                        System.out.println("---------------------------------------------");
                        break;

                    case 4:
                        flag = false;
                        break;

                    default:
                        throw new ErrorHandler("Please input from numbers from 1-4 only.  Thank you!");
                }
            } catch(ErrorHandler e){
                System.out.println("Error: " + e.getMessage());
            } catch(Exception e){
                //e.printStackTrace();
                System.out.println("Error: Value must be a number or must not be empty. Thank you!");
                flag = continuePrompt();
            }
        }
    }

    public static boolean continuePrompt() {
        char choice;
        System.out.println("Do you want to Continue? Y/N");
        try {
            choice = scan.nextLine().toUpperCase().charAt(0);
        } catch (Exception err) {
            System.out.println("Value must be Y or N only or must NOT be empty");
            System.out.println("Program will stop.");
            return false;
        }
        
        if ( choice == 'Y'){
            return true;
        } else {
            return false;
        }
    }

    
    public static void displayCustomers() {
        ArrayList<Customer> customers = customerManager.getAllCustomers();
        int count = 0;
        System.out.println("================== Customer Records ==================");
        for (Customer customer : customers) {
            System.out.println("Index: " + count + " [ Customer ID: " + customer.getCustomer_id() + " ][ Customer Name: " + customer.getCustomer_name() + " ]");
            count++;
        }
        System.out.println("======================================================");
    }

    public static void createCustomer(int customer_id, String customer_name) {
        Customer customer = new Customer();
        customer.setCustomer_id(customer_id);
        customer.setCustomer_name(customer_name);
        customerManager.createCustomer(customer);
    }

    public static Customer retrieveCustomer(int customer_id) {
        Customer customer = customerManager.retrieveCustomer(customer_id);
        if (customer != null) {
            System.out.println("Retrieved Customer with ID: " + customer.getCustomer_id());
            System.out.println("[ ID: " + customer.getCustomer_id() + " ][ Name: " + customer.getCustomer_name() + " ]");
        }
        return customer;
    }

    public static void deleteCustomer(int customer_id) {
        Customer customer = customerManager.retrieveCustomer(customer_id);
        if (customer == null) {
            System.out.println("Customer with ID " + customer_id + " does not exist!");
        } else {
            customerManager.deleteCustomer(customer_id);
            System.out.println("Customer with ID " + customer_id + " is deleted");
        }
    }
    
    public static void displayInvoices(int customer_id){
        InvoiceCRUD invoiceManager = new InvoiceCRUD(customer_id); 
        ArrayList<Invoice> invoices = invoiceManager.getAllInvoices();
        double subtotal = 0;
        System.out.println("================== Invoice Records ==================");
        System.out.println("         THIS SERVES AS YOUR OFFICIAL RECEIPT        ");
        System.out.println("           THANK YOU, AND PLEASE COME AGAIN          ");
        System.out.println("                 KC FOODS CORPORATION                ");
        System.out.println("                       KC MART                       ");
        System.out.println("                TRENTO, AGUSAN DEL SUR               ");
        System.out.println("-----------------------------------------------------");
        System.out.println(String.format("%6s %-20s %-3s %10s %10s", 
                                "ID", "ITEM", "QTY", "PRICE", "TOTAL"));
        for (Invoice invoice : invoices ) {
            double total = invoice.getInvoice_qty() * invoice.getInvoice_price();
            subtotal += total; 
        System.out.println(String.format("%6d %-20s %-3d %10.2f %10.2f", 
                                        invoice.getInvoice_id(), 
                                        invoice.getInvoice_item(), 
                                        invoice.getInvoice_qty(), 
                                        invoice.getInvoice_price(), 
                                        total));
        }
        double taxrate = 0.12;
        double tax = subtotal * taxrate;
        double total = subtotal + tax;
        System.out.println(" ");
        System.out.println("AMOUNT DUE:                             " + String.format("%13.2f", total));
        System.out.print("CASH:                                          ");
        double amount_paid = Double.parseDouble(scan.nextLine());
        double change = amount_paid - total;
        System.out.println("CHANGE:                                 " + String.format("%13.2f", change));
        System.out.println(" ");
        System.out.println("VATABLE SALES:                          " + String.format("%13.2f", subtotal));
        System.out.println("VAT-EXEMPT SALES:                       " + String.format("%13.2f", 0.00));
        System.out.println("VAT ZERO-RATED SALES:                   " + String.format("%13.2f", 0.00));
        System.out.println("VAT AMOUNT (12%):                       " + String.format("%13.2f", tax));
        System.out.println("-----------------------------------------------------");
        System.out.print("CASHIER: ");
        String cashier = scan.nextLine().toUpperCase();
        LocalDateTime currentdatetime = LocalDateTime.now();
        LocalDateTime validuntildatetime = currentdatetime.plus(5, ChronoUnit.YEARS);
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatterdate = currentdatetime.format(dateformatter);
        System.out.print("DATE: " + formatterdate);
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("hh:mm:ssa");
        String formattertime = currentdatetime.format(timeformatter);
        System.out.println("               TIME: " + formattertime);
        System.out.println("-----------------------------------------------------");
        System.out.println("         THIS SERVES AS YOUR OFFICIAL RECEIPT        ");
        System.out.println("             TELL US ABOUT YOUR EXPERIENCE           ");
        System.out.println("      SEND US FEEDBACK AT KLYNECHRISLU@GMAIL.COM     ");
        System.out.println("              VISIT US AT KLYNE CHRYSLER             ");
        System.out.println("           THANK YOU, AND PLEASE COME AGAIN          ");
        System.out.println("-----------------------------------------------------");
        System.out.println("CUST NAME:_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        System.out.println("ADDRESS:_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        System.out.println("        _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        System.out.println(" TIN#:_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        System.out.println("BUS STYLE:_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        System.out.println("               KC COMPUTER SCIENCE, INC              ");
        System.out.println("                TRENTO, AGUSAN DEL SUR               ");
        System.out.println("                DATE ISSUED " + formatterdate);
        String validuntil = validuntildatetime.format(dateformatter);
        System.out.println("              AND VALID UNTIL " + validuntil);
        System.out.println("         THIS INVOICE RECEIPT SHALL BE VALID         ");
        System.out.println("         FOR FIVE (5) YEARS FROM THE DATE OF         ");
        System.out.println("                  THE PERMIT TO USE                  ");
        System.out.println("=====================================================");
    }

    public static void createInvoice(int customer_id, int invoice_id, String invoice_item, int invoice_qty, double invoice_price){
        InvoiceCRUD invoiceManager = new InvoiceCRUD(customer_id);
        Invoice invoice = new Invoice();
        invoice.setInvoice_id(invoice_id);
        invoice.setInvoice_item(invoice_item);
        invoice.setInvoice_qty(invoice_qty);
        invoice.setInvoice_price(invoice_price);
        invoiceManager.createInvoice(invoice);
    }

    public static Invoice retrieveInvoice(int customer_id, int invoice_id) {
        InvoiceCRUD invoiceManager = new InvoiceCRUD(customer_id);
        Invoice record = invoiceManager.retrieveInvoice(invoice_id);
        if (record == null) {
            return record;
        }
        System.out.println("Retrieved Invoice Record with ID: " + invoice_id);
        System.out.println("---------------------------------------------");
        System.out.println("Invoice ID:     " + record.getInvoice_id());
        System.out.println("Item Name:      " + record.getInvoice_item());
        System.out.println("Item Qty:       " + record.getInvoice_qty());
        System.out.println("Item Price:     " + record.getInvoice_price());
        return record;
    }

    public static void updateInvoice(int customer_id, int invoice_id, double invoice_price) {
        InvoiceCRUD invoiceManager = new InvoiceCRUD(customer_id);
        Invoice invoice = invoiceManager.retrieveInvoice(invoice_id);
        if (invoice == null) {
            System.out.println("Invoice with ID " + invoice_id + " does not exist!");
        } else {
            invoice.setInvoice_price(invoice_price);
            invoiceManager.updateInvoice(invoice);
            System.out.println("Invoice with ID " + invoice_id + " updated successfully!");
        }
    }
}