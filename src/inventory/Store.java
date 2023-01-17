/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

/**
 Name: <Ernst Thelifort>
 Course: CNT 4714 –
 Spring 2021 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 31, 2021
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.*;
import java.util.*;

public class Store extends JFrame {
	private String inventoryFile = "inventory.txt";
	private ArrayList<Item> inventory;
	private Invoice order = new Invoice();
	
	// create Text Fields
	private JTextField jtfNumItems = new JTextField();
	private JTextField jtfItemID = new JTextField();
	private JTextField jtfQuantity = new JTextField();
	private JTextField jtfItemInfo = new JTextField();
	private JTextField jtfTotalItems = new JTextField();

	// create Buttons
	private JButton jbtProcessItem = new JButton("Process Item #1");// need to update item #


	private JButton jbtConfirmItem = new JButton("Confirm Item #1");// need to update item #
	private JButton jbtViewInvoice = new JButton("View Order");
	private JButton jbtFinishInvoice = new JButton("Finish Order");
	private JButton jbtNewInvoice = new JButton("New Order");
	private JButton jbtExit = new JButton("Exit");

	// create Jlabels
	JLabel jlbSubtotal = new JLabel("Order Subtotal for 0 item(s):", SwingConstants.RIGHT);
	JLabel jlbItemID = new JLabel("Enter Item ID for Item #1:", SwingConstants.RIGHT);
	JLabel jlbQuantity = new JLabel("Enter Quantitiy for Item #1:", SwingConstants.RIGHT);
	JLabel jlbItemInfo = new JLabel("Item #1 Info:", SwingConstants.RIGHT);

	// constuctor
	public Store() throws FileNotFoundException 
	{
		this.getInventoryFromFile();
		//panel p1 holds textfields and labels
		//p1 will have a gridlayout of 5 rows and 2 columns
		
                JPanel p1 = new JPanel(new GridLayout(5,2));
                p1.setBackground(Color.black);
		JLabel temp = new JLabel("Enter number of items in this order:", SwingConstants.RIGHT);
                temp.setForeground(Color.yellow);
                p1.add(temp);
                
                jlbSubtotal.setForeground(Color.yellow);
                jlbItemID.setForeground(Color.yellow);
                jlbQuantity.setForeground(Color.yellow);
                jlbItemInfo.setForeground(Color.yellow);
		
                p1.add(jtfNumItems);
		p1.add(jlbItemID);
		p1.add(jtfItemID);
		p1.add(jlbQuantity);
		p1.add(jtfQuantity);
		p1.add(jlbItemInfo);
		p1.add(jtfItemInfo);
		p1.add(jlbSubtotal);
		p1.add(jtfTotalItems);
		
		
		
		//panel p2 holds the six buttons
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                p2.setBackground(Color.blue);
		p2.add(jbtProcessItem);
		p2.add(jbtConfirmItem);
		p2.add(jbtViewInvoice);
		p2.add(jbtFinishInvoice);
		p2.add(jbtNewInvoice);
		p2.add(jbtExit);
		
		
		//deactivate buttons
		this.jbtConfirmItem.setEnabled(false);
		this.jbtViewInvoice.setEnabled(false);
		this.jbtFinishInvoice.setEnabled(false);
		
		//deactivate textfields
		this.jtfTotalItems.setEnabled(false);
		this.jtfItemInfo.setEnabled(false);
		
		//add the panels to the frame
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.SOUTH);
		
		
		//actionlisteners for all buttons
		jbtProcessItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//get info
				int numOfItemsInInvoice = Integer.parseInt(jtfNumItems.getText());
				String itemID = jtfItemID.getText();
				int quantityOfItem = Integer.parseInt(jtfQuantity.getText());
				
				//set maxNumofItems for  new order
				if(order.getMaxNumItems() == -1 && numOfItemsInInvoice > 0) {
					order.setMaxNumItems(numOfItemsInInvoice);
					jtfNumItems.setEnabled(false);
				}
				//search for item
				int itemIndex = linearSearch(itemID);
				//found item
				if(itemIndex != -1) 
				{
					// give this item info to order to print

					Item foundItem = inventory.get(itemIndex);
                                        if(foundItem.isInStock()){
                                            order.setItemInfo(foundItem.getItemID() + "", foundItem.getTitle(),  new DecimalFormat("#0.00").format(foundItem.getPrice()) + "", quantityOfItem + "", order.getDiscountPercentage(quantityOfItem) + "",  new DecimalFormat("#0.00").format(order.getTotalDiscount(quantityOfItem, foundItem.getPrice())) + "");
                                            String itemInfo = foundItem.getItemID() + foundItem.getTitle() +  " $" + new DecimalFormat("#0.00").format(foundItem.getPrice()) + " " + quantityOfItem + " " + order.getDiscountPercentage(quantityOfItem) + "% " + "$" +  new DecimalFormat("#0.00").format(order.getTotalDiscount(quantityOfItem, foundItem.getPrice())); //need to get discound
                                            jtfItemInfo.setText(itemInfo);
                                            jbtConfirmItem.setEnabled(true);
                                            jbtProcessItem.setEnabled(false);
                                            order.setOrderSubtotal(quantityOfItem, foundItem.getPrice());
                                            jtfItemInfo.setEnabled(false);
                                            jtfTotalItems.setEnabled(false);
                                        }
                                        else 
                                        {
                                            JOptionPane.showMessageDialog(null, "Sorry... that item is out of stock, please try another item");
                                        }
				}
				//item not found display alert
				else 
				{
					JOptionPane.showMessageDialog(null, "Item ID " + itemID + " not in file.");
				}
			}
			
		});
		
		
		//action on each button
		jbtConfirmItem.addActionListener(new ActionListener(){		
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				int numOfItemsInInvoice = Integer.parseInt(jtfNumItems.getText());
				String itemID = jtfItemID.getText();
				int quantityOfItem = Integer.parseInt(jtfQuantity.getText());
				
				//increment currentNumofItems
				order.setCurrentNumItems(1);
				//update subtotal items
				order.setTotalItems(order.getTotalItems() + 1);
				
				JOptionPane.showMessageDialog(null, "Item #" + order.getTotalItems() + " accepted");
				
				//prepare transaction.txt line
				order.prepareTransaction();
				
				//add item to viewInvoice
				order.addToViewOrder(jtfItemInfo.getText());
				
			
				//enable buttons
				jbtProcessItem.setEnabled(true);
				jbtViewInvoice.setEnabled(true);
				jbtFinishInvoice.setEnabled(true);
				jbtConfirmItem.setEnabled(false);
				jtfNumItems.setEnabled(false);
				
				//update button text
				jbtProcessItem.setText("Process Item #" + (order.getTotalItems() + 1));
				jbtConfirmItem.setText("Confirm Item #" + (order.getTotalItems() + 1));
				
				//update textFields
				jtfItemID.setText("");
				jtfQuantity.setText("");
				jtfTotalItems.setText("$" +  new DecimalFormat("#0.00").format(order.getOrderSubtotal()));
				
				//update labels
				jlbSubtotal.setText("Invoice subtotal for " + order.getCurrentNumItems() + " item(s)");
				jlbItemID.setText("Enter Item ID for Item #" + (order.getTotalItems() + 1) + ":");
				jlbQuantity.setText("Enter quantity for Item #" + (order.getTotalItems() + 1) + ":");
				if(order.getCurrentNumItems() < order.getMaxNumItems())
				jlbItemInfo.setText("Item #" + (order.getTotalItems() + 1) + " info:");
				
				//final item order
				if(order.getCurrentNumItems() >= order.getMaxNumItems()) {
					jlbItemID.setVisible(false);
					jlbQuantity.setVisible(false);
					jbtProcessItem.setEnabled(false);
					jbtConfirmItem.setEnabled(false);
				}
			}
		
		
		});
		
		
		jbtViewInvoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, order.getViewOrder());
			}
		});
		
		
		
		jbtFinishInvoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//write items to transactions.txt
				try {
					order.printTransactions();
					JOptionPane.showMessageDialog(null, order.getFinishOrder());

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Store.super.dispose(); //dispose frame
			}
		});
		
		
		jbtNewInvoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				Store.super.dispose(); //dispose frame
				//run main
				try {
					Store.main(null);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		jbtExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Store.super.dispose(); //dispose frame
			}
		});
		
	}


	public int linearSearch(String ItemID) {
		for(int i = 0; i < this.inventory.size(); i++) {
			Item currentItem = inventory.get(i);
			if(currentItem.getItemID().equals(ItemID))
				return i;
		}
		return -1;
	}
	
	public void getInventoryFromFile() throws FileNotFoundException {
		// create load all items in an arraylist
		this.inventory = new ArrayList<Item>();
		File file = new File("inventory.txt");
		Scanner textFile = new Scanner(file);

		// scan in inventory into arraylist
		while (textFile.hasNextLine()) {
			// grab next inventory line and parse into 3 strings
			String item = textFile.nextLine();
			String[] itemInfo = item.split(",");

			// create a item and set fields
			Item currentItem = new Item();
			currentItem.setItemID(itemInfo[0].trim());

			currentItem.setTitle(itemInfo[1].trim());

                        if(itemInfo[2].trim().equals("true"))
                            currentItem.setInStock(true);
                        else
                            currentItem.setInStock(false);
                        
			currentItem.setPrice(Double.parseDouble(itemInfo[3].trim()));

			// add item to inventory arraylist
			inventory.add(currentItem);
		}
		
		//close stream
		textFile.close();
		// testing
		for (int i = 0; i < inventory.size(); i++) {
			Item current = inventory.get(i);
			System.out.println(current.getItemID() + ", " + current.getTitle() + ", " + current.isInStock()+ ", " + current.getPrice());
		}
	}

	
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		Store frame = new Store();
		frame.pack(); // fit windows for screen
		frame.setTitle("Nile Dot Com");
		frame.setLocationRelativeTo(null); // center windows
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // close window
		frame.setVisible(true); // display window
	}
}