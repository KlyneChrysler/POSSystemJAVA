import java.io.*;
import java.util.*;
public class InvoiceCRUD{
    File invoices;
    public InvoiceCRUD(int customer_id){
        invoices = new File(customer_id + "_invoice.txt");
    }

    public ArrayList<Invoice> getAllInvoices() {
        ArrayList <Invoice> invoice_list = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new FileReader(invoices));
            while(scan.hasNext()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoice_id(scan.nextInt());
                    invoice.setInvoice_item(scan.next());
                    invoice.setInvoice_qty(scan.nextInt());
                    invoice.setInvoice_price(scan.nextDouble());
                    invoice_list.add(invoice);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return invoice_list;
    }

    public void createInvoice(Invoice invoice){
        boolean flag = true;
        ArrayList<Invoice> invoice_list = getAllInvoices();
        FileWriter fw;
        BufferedWriter bw;
        for (Invoice record : invoice_list) {
            try{
                if(record.getInvoice_id() == invoice.getInvoice_id()){
                    throw new ErrorHandler("ID already used.");
                }
            } catch(ErrorHandler e){
                System.out.println("Error: " + e.getMessage());
            }
        }

        if(flag){
            try {
                fw = new FileWriter(invoices, true);
                bw = new BufferedWriter(fw);
                bw.write(invoice.getInvoice_id() + "");
                bw.write(" ");
                bw.write(invoice.getInvoice_item() + "");
                bw.write(" ");
                bw.write(invoice.getInvoice_qty() + "");
                bw.write(" ");
                bw.write(invoice.getInvoice_price() + "");
                bw.write("\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Invoice retrieveInvoice( int id ) {
        Invoice record = new Invoice( );
        ArrayList <Invoice> invoice_list = getAllInvoices();
        for ( Invoice invoice : invoice_list ) {
            if ( invoice.getInvoice_id() == id ) {
                record.setInvoice_id(invoice.getInvoice_id());
                record.setInvoice_item(invoice.getInvoice_item());
                record.setInvoice_qty(invoice.getInvoice_qty());
                record.setInvoice_price(invoice.getInvoice_price());
                return record;
            }
        }
        return null;
    }

    public void updateInvoice (Invoice updatedInvoice) {
        ArrayList <Invoice> invoice_list = getAllInvoices();
        for (Invoice invoice : invoice_list) {
            if (invoice.getInvoice_id() == updatedInvoice.getInvoice_id()) {
                invoice_list.set(invoice_list.indexOf(invoice), updatedInvoice);
                break;
            }
        }
        String format = "";
        for ( Invoice invoice : invoice_list ) {
            format += invoice.getInvoice_id() + " " + invoice.getInvoice_item() + " " + invoice.getInvoice_qty() + " " +invoice.getInvoice_price() + " " + "\n";
        }
        try {
            Formatter formatFile = new Formatter(invoices);
            formatFile.format("%S", format);
            formatFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteInvoice(){
        invoices.delete();
        System.out.println(invoices.getName() + " deleted.");
    }
}