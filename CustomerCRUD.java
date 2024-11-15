import java.io.*;
import java.util.*;
public class CustomerCRUD{
    InvoiceCRUD invoiceManager;
    File customers;
    public CustomerCRUD() {
        customers = new File("Customers.txt");
    }
    
    public ArrayList<Customer> getAllCustomers(){
        ArrayList<Customer> customer_list = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new FileReader(customers));
            while(scan.hasNext()){
                try {
                    Customer customer = new Customer();
                    customer.setCustomer_id(scan.nextInt());
                    customer.setCustomer_name(scan.next());
                    customer_list.add(customer);
                }catch (Exception e) {
                    continue;
                }  
            }   
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return customer_list;
    }

    public void createCustomer(Customer customer) {
        boolean flag = true;
        ArrayList<Customer> customerList = getAllCustomers();
        FileWriter fw;
        BufferedWriter bw;
        for (Customer record : customerList) {
            try {
                if (record.getCustomer_id() == customer.getCustomer_id()) {
                    throw new ErrorHandler("ID is already used.");
                }
            } catch (ErrorHandler err) {
                flag = false;
                System.out.println("Error: " + err.getMessage());
            }
        }

        if (flag) {
            try {
                fw = new FileWriter(customers, true);
                bw = new BufferedWriter(fw);
                bw.write(customer.getCustomer_id() + "");
                bw.write(" ");
                bw.write(customer.getCustomer_name().toUpperCase());
                bw.write("\n");
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Customer retrieveCustomer(int id) {
        Customer record = new Customer();
        ArrayList<Customer> customer_list = getAllCustomers();
        for (Customer customer : customer_list) {
            if (customer.getCustomer_id() == id) {
                record.setCustomer_id(customer.getCustomer_id());
                record.setCustomer_name(customer.getCustomer_name());
                return record;
            }
        }
        return null;
    }

    public void deleteCustomer (int id) {
        ArrayList <Customer> customer_list = getAllCustomers();
        for (Customer customer : customer_list) {
            if(customer.getCustomer_id() == id) {
                customer_list.remove(customer);
                invoiceManager = new InvoiceCRUD(id);
                invoiceManager.deleteInvoice();
                break;
            }
        }
        String format = "";
        for (Customer customer : customer_list) {
            format += customer.getCustomer_id() + " " + customer.getCustomer_name().toUpperCase() + "\n"; 
        }
        try {
            Formatter formatFile = new Formatter(customers);
            formatFile.format("%S", format);
            formatFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}