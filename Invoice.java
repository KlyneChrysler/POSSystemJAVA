public class Invoice {

    private int invoice_id;
    private String invoice_item;
    private int invoice_qty;
    private double invoice_price;

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }
 
    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_item(String invoice_item) {
        this.invoice_item = invoice_item;
    } 
    
    public String getInvoice_item() {
        return invoice_item;
    }

    public void setInvoice_qty(int invoice_qty) {
        this.invoice_qty = invoice_qty;
    } 
    
    public int getInvoice_qty() {
        return invoice_qty;
    }

    public void setInvoice_price(double invoice_price) {
        this.invoice_price = invoice_price;
    } 
    
    public double getInvoice_price() {
        return invoice_price;
    }
}


