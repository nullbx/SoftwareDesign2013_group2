/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.structurePanel;

import graphview.GraphEdge;
import graphview.GraphNode;
import graphview.GraphScene;
import graphview.shapes.NodeAspect;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.CellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author FlyPig
 */
public class StructurePanel extends javax.swing.JPanel {

    private GraphScene scene;
    public DefaultTableModel modelNode;
    public DefaultTableModel modelEdge;
    
    public JTable tableNode;
    public JTable tableEdge;
    
//    private ArrayList<TableCellEditor> editors = new ArrayList<TableCellEditor>();
//    private ArrayList<TableCellEditor> editorsTo = new ArrayList<TableCellEditor>();
//    private ArrayList<TableCellEditor> editorsDir = new ArrayList<TableCellEditor>();
//    
    public TablePropertiesFrame propertiesFrame;
    
    private JFrame mainFrame;
    
    private JPopupMenu getJPopupMenuNodeIndividual = null;
    private JPopupMenu getJPopupMenuNodeMany = null;
    private JPopupMenu getJPopupMenuEdgeIndividual = null;
    private JPopupMenu getJPopupMenuEdgeMany = null;

    private JMenuItem jMenuItemDelete = null;
    
//    private ListSelectionModel SelectionModelNode;
    
    private JDialog textDialog=null;
    
    private Point editingNodeCell=new Point();
    private Point editingEdgeCell=new Point();
    
    private JTextArea textField=new JTextArea();
    
    private Point cellNodeToEdit=new Point();
    private Point cellEdgeToEdit=new Point();
    
    private nodeCreateDialog nodeCreate=null;
    
//    private ArrayList<Integer> arraySelectedNode=new ArrayList<Integer>();
    
    private JComboBox comboBoxNodesId = new JComboBox();
    private JComboBox comboBoxDir = new JComboBox();
    
    
    
    
    /**
     * Creates new form StructurePanel
     */
    public StructurePanel( GraphScene scene_, JFrame parent) {
        scene=scene_;      
        mainFrame=parent;
        propertiesFrame=new TablePropertiesFrame(parent, true);
        initComponents();
        initLayout();      
        initTextDialog();
    }

    public void initLayout()
    {       
        jButton1.setSelected(true);
        tableNode=createNodeTable();
        tableEdge=createEdgeTable();
        jScrollPane1.setViewportView(tableNode);
        updateNodes();        
        initBox();
               
    } 
    
    private void initBox()
    {
        comboBoxDir.addItem("True");
        comboBoxDir.addItem("False");
        comboBoxDir.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {                    
                    Object item = event.getItem();
                    int row=-1;
                    int col=-1;
                    row=(int) editingEdgeCell.getX();
                    col=(int) editingEdgeCell.getY();
                    if((String)tableEdge.getModel().getValueAt(row,col)!=(String)item)
                    {
                        
                        int edgeId=(Integer)tableEdge.getModel().getValueAt(row, 0);
                        boolean dir=false;
    
                        if((String)item=="True")
                        {
                            dir=true;
                        }
                        else
                        {
                            dir=false;
                        }
                        scene.getEdge(edgeId).setDirection(dir);
                    }                 
                }
            }  
        });
    }
        
    
    private void initTextDialog()
    {
        nodeCreate=new nodeCreateDialog(mainFrame, true);
        
        Dimension texpPanelSize= new Dimension(300, 300) ;
        textDialog=new JDialog(mainFrame,"Text", true);
        
        textDialog.setSize((int)texpPanelSize.getWidth(), (int)texpPanelSize.getHeight()+100 );   
        textField.setMinimumSize(texpPanelSize);
        textField.setSize(texpPanelSize);   
        Font myFront=textField.getFont();
        Font newFront=new Font(myFront.getName(), myFront.getStyle(),myFront.getSize()+5 );
        textField.setFont(newFront);
        JScrollPane textScrollPanel = new JScrollPane(textField);
        textScrollPanel.setSize(texpPanelSize);
        JPanel textPanel=new JPanel();
        textPanel.add(textScrollPanel);
        textPanel.setSize((int)texpPanelSize.getWidth(), (int)texpPanelSize.getHeight()-200 );  
        textPanel.setLayout(new BorderLayout());
        JButton enter = new JButton("Enter");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(jButton1.isSelected())
                {
                    String label=textField.getText();
                    int ID=(int)tableNode.getModel().getValueAt((int)editingNodeCell.getX(), 0);
                    scene.getNode(ID).getAspect().setLabel(label);            
                    textDialog.setVisible(false);
//                    tableNode.getModel().setValueAt(textField.getText(), (int)editingCell.getX(), (int)editingCell.getY());
//                    tableNode.getCellEditor().stopCellEditing();
//                    tableNode.setEnabled(false);
                    updateScene();
                }
                else
                {
                    String label=textField.getText();
                    int ID=(int)tableEdge.getModel().getValueAt((int)editingNodeCell.getX(), 0);
                    scene.getEdge(ID).getAspect().setLabel(label);            
                    textDialog.setVisible(false);
                    updateScene();
                }
               
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
                textDialog.setVisible(false);
            }
        });
        textPanel.add(enter,BorderLayout.PAGE_START);
        textPanel.add(cancel,BorderLayout.PAGE_END);
 
        
        textDialog.setLayout(new BorderLayout());
        textDialog.add(textScrollPanel);
        textDialog.add(textPanel,BorderLayout.AFTER_LAST_LINE);
        
         
        
       // textDialog.setResizable(false);
    }    
    
    public void startEdit()
    {
        textDialog.setLocation(
                mainFrame.getLocationOnScreen().x+
                mainFrame.getWidth()/2-
                textDialog.getWidth()/2,
                mainFrame.getLocationOnScreen().y+
                mainFrame.getHeight()/2-
                textDialog.getHeight()/2 
                );
        int row;
        int col;
        if(jButton1.isSelected())
        {
            row=tableNode.getSelectedRow();
            col=tableNode.getSelectedColumn();
            editingNodeCell.x=row;
            editingNodeCell.y=col;
            String data=(String)tableNode.getModel().getValueAt(row, col);
            textField.setText(data);

            textDialog.setVisible(true);
        }
        else
        {
            row=tableEdge.getSelectedRow();
            col=tableEdge.getSelectedColumn();
            editingEdgeCell.x=row;
            editingEdgeCell.y=col;
            String data=(String)tableEdge.getModel().getValueAt(row, col);
            textField.setText(data);

            textDialog.setVisible(true);
        }
       
        
    }
    
 
    
    private JTable createNodeTable()
    {
        modelNode = new DefaultTableModel(){  
            @Override  
            public boolean isCellEditable(int row, int column){  
                return true;  
            };     
        };
        modelNode.addColumn("Node ID");
        modelNode.addColumn("Label");
       // modelNode.addColumn("Edge's ID");
       // modelNode.addColumn("Shape");
        
        //ListSelectionModel lm = new DefaultListSelectionModel();
        //lm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        final JTable table = new JTable(modelNode)
        {
           /* public TableCellEditor getCellEditor(int row, int column)
            {               
                if (column == 2)
                    return editors.get(row);
                else
                    return super.getCellEditor(row, column);
            }*/
            
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1)
                {
                    return false;                    
                }
                else
                {
                    return true;
                }
            }
        
//        
//        @Override
//        
//        public boolean isEditing()
//        {
//            System.out.printf("%d\t%d\n",selectionModel.getMinSelectionIndex(),columnModel.getSelectionModel().getMinSelectionIndex());
//            return cellEditor != null;
//        }
                
        };
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {                                                   
                if (SwingUtilities.isLeftMouseButton(e)) 
                {
                    Point p = e.getPoint();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    if(column!=-1 && row!=-1) 
                    { 
                        table.changeSelection(row, column, true, true);                                  
                    }  
                    
                   
                    if(table.getSelectedColumnCount()==1)
                    {
                        if(column== (int)cellNodeToEdit.getX() && row== (int)cellNodeToEdit.getY() && column==1 )
                        {
                            cellNodeToEdit.x=0;
                            cellNodeToEdit.y=0;
                            startEdit();
                        }
                        else
                        {
                            cellNodeToEdit.x=column;
                            cellNodeToEdit.y=row;
                        }
                    }
                    
                    
                }                
                else if (SwingUtilities.isRightMouseButton(e))
                {      
                    Point p = e.getPoint();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    boolean select=false;
                    if(table.getSelectedRowCount()==0)
                    {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    else
                    {
                        int[] selectedRows=table.getSelectedRows();
                        for(int i=0; i<selectedRows.length; i++)
                        {
                            if(selectedRows[i]==row)                            
                            {
                                select=true;
                            }
                        }
                        if(select==false)
                        {
                            table.clearSelection();
                            table.setColumnSelectionInterval(column, column);
                            table.setRowSelectionInterval(row, row);
                        }                        
                    }
                    JPopupMenu jPopupMenu = getJPopupMenu();
                    jPopupMenu.show(table, e.getX(), e.getY());
                }               
            }
        });
//        table.addPropertyChangeListener(new PropertyChangeListener() 
//        {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if ("tableCellEditor".equals(evt.getPropertyName()))
//		{
//			if (table.isEditing())
//                        {                            
//                            int row=table.getSelectedRow();
//                            int col=table.getSelectedColumn();
//                            editingCell.x=row;
//                            editingCell.y=col;
//                            String data=(String)table.getModel().getValueAt(row, col);
//                            textField.setText(data);
//                            textDialog.setLocation(
//                                    mainFrame.getLocationOnScreen().x+
//                                    mainFrame.getWidth()/2-
//                                    textDialog.getWidth()/2,
//                                    mainFrame.getLocationOnScreen().y+
//                                    mainFrame.getHeight()/2-
//                                    textDialog.getHeight()/2 
//                                    );  
//                            textDialog.setVisible(true);
//                        }
//		}
//            }
//        });

//        TableColumn column = table.getColumnModel().getColumn(1);
//        column.setCellEditor(new DefaultCellEditor(new JTextField()));
//        column.getCellEditor().addCellEditorListener(new CellEditorListener()
//        {
//            int row;
//            String label;
//            int ID;
//            @Override
//            public void editingCanceled(ChangeEvent e)                
//            {
//                row = table.getSelectedRow();
//                ID=(int)table.getModel().getValueAt(row, 0);
//                label=(String)table.getModel().getValueAt(row, 1);
//                scene.getNode(ID).getAspect().setLabel(label);
//            }
//            @Override
//            public void editingStopped(ChangeEvent e) 
//            {                
//                row = table.getSelectedRow();
//                ID=(int)table.getModel().getValueAt(row, 0);
//                label=(String)table.getModel().getValueAt(row, 1);
//                scene.getNode(ID).getAspect().setLabel(label);
//            }
//        }
//                );

        //table.setSelectionModel(lm);
        return table;
    }
    
    public void updateScene()
    {
        updateNodes();
        updateEdges();
    }
    
    private JTable createEdgeTable()
    {
        modelEdge = new DefaultTableModel(){  
            @Override  
            public boolean isCellEditable(int row, int column){  
                return true;  
            };     
        };
        modelEdge.addColumn("Edge ID");
        modelEdge.addColumn("From");
        modelEdge.addColumn("To");        
        modelEdge.addColumn("Bdirectional");
        modelEdge.addColumn("Label");
         
        final JTable table = new JTable(modelEdge)
        {
            @Override
            public TableCellEditor getCellEditor(int row, int column)
            {        
                editingEdgeCell.x=row;
                editingEdgeCell.y=column;
                int edgeId=-1;
                DefaultCellEditor dce;
                if (column == 1 )
                {
                    edgeId=(Integer)tableEdge.getModel().getValueAt(row, 0);
                    comboBoxNodesId.setSelectedItem((Integer.toString(scene.getEdge(edgeId).getFromID())));
                    tableEdge.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBoxNodesId));
                    dce = new DefaultCellEditor( comboBoxNodesId );                    
                    return dce;
                }
                else if(column == 2)
                {
                    edgeId=(Integer)tableEdge.getModel().getValueAt(row, 0);
                    comboBoxNodesId.setSelectedItem(Integer.toString(scene.getEdge(edgeId).getToID()));
                    tableEdge.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxNodesId));
                    dce = new DefaultCellEditor( comboBoxNodesId );  
                    return dce; 
                }
                else if(column == 3)
                {
                    edgeId=(Integer)tableEdge.getModel().getValueAt(row, 0);
                    if(scene.getEdge(edgeId).isDirectional())
                    {
                        comboBoxDir.setSelectedItem("True");
                    }
                    else
                    {
                        comboBoxDir.setSelectedItem("False");
                    }
                    tableEdge.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBoxDir));
                    dce = new DefaultCellEditor( comboBoxDir );  
                    return dce;
                }
//                tableEdge.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBoxNodesId));
//                tableEdge.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxNodesId));
//                tableEdge.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBoxDir));
//                DefaultCellEditor dce = new DefaultCellEditor( comboBoxNodesId );
//                editors.add( dce ); 
//                dce = new DefaultCellEditor( comboBoxDir );
//                editors.add( dce );
                else
                {
                    return super.getCellEditor(row, column);
                }
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 4)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }         
        };
        
            
       
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {                                                   
                if (SwingUtilities.isLeftMouseButton(e)) 
                {
                    Point p = e.getPoint();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    if(column!=-1 && row!=-1) 
                    { 
                        table.changeSelection(row, column, true, true);                                  
                    }  
                    
                   
                    if(table.getSelectedColumnCount()==1)
                    {
                        if(column== (int)cellEdgeToEdit.getX() && row== (int)cellEdgeToEdit.getY() && column==4 )
                        {
                            cellEdgeToEdit.x=0;
                            cellEdgeToEdit.y=0;
                            startEdit();
                        }
                        else
                        {
                            cellEdgeToEdit.x=column;
                            cellEdgeToEdit.y=row;
                        }
                    }
                    
                    
                }                
                else if (SwingUtilities.isRightMouseButton(e))
                {      
                    Point p = e.getPoint();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    boolean select=false;
                    if(table.getSelectedRowCount()==0)
                    {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    else
                    {
                        int[] selectedRows=table.getSelectedRows();
                        for(int i=0; i<selectedRows.length; i++)
                        {
                            if(selectedRows[i]==row)                            
                            {
                                select=true;
                            }
                        }
                        if(select==false)
                        {
                            table.clearSelection();
                            table.setColumnSelectionInterval(column, column);
                            table.setRowSelectionInterval(row, row);
                        }                        
                    }         
                        JPopupMenu jPopupMenu = getJPopupMenu();
                        jPopupMenu.show(table, e.getX(), e.getY());                                    
                }               
            }
        });
        
      
        
//        table.addPropertyChangeListener(new PropertyChangeListener() 
//        {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if ("tableCellEditor".equals(evt.getPropertyName()))
//		{
//                    int row;
//                    int col;
//			if (table.isEditing())
//                        {                            
//                            row=(int) editingEdgeCell.getX();
//                            col=(int) editingEdgeCell.getY();
//
//                            JComboBox myCombo=(JComboBox)table.getEditorComponent();
//                            if(col==3)
//                            {                   
//                                boolean dir=false;
//                                int ID=-1;            
//                                ID=(int)table.getModel().getValueAt(row, 0);
//                                String lol=(String)table.getModel().getValueAt(row, 3);
//                                if((String)table.getModel().getValueAt(row, 3)=="True")
//                                {
//                                    dir=true;
//                                }
//                                else
//                                {
//                                    dir=false;
//                                }
//                                scene.getEdge(ID).setDirection(dir);
//                                dir=false;
//                            }
//                        }                       
//                }          
//            };
// });
        
        return table;
    }
   
    public void updateNodes()
    {
        //editors.clear();
        while(modelNode.getRowCount()>0)
        {
            modelNode.removeRow(0);
        }
        tableNode.setModel(modelNode);
        for(int i=0;i<scene.getSizeNodeArray();i++)
        {
            if(scene.getNode(i)!=null)
            {

//                JComboBox comboBox = new JComboBox();
//                for(int j=0; j<node.getSizeOfNodeEdgesIDArray();j++)
//                {
//                    comboBox.addItem(String.valueOf(node.getElementOfNodeEdgesIDArray(j)));
//                }
                
//                if(node.getSizeOfNodeEdgesIDArray()>0)
//                {
//                    Object[] rowData={
//                        node.getID(),
//                        "",
//                        String.valueOf(node.getElementOfNodeEdgesIDArray(0)),
//                        node.getAspect()
//                    };
//                    modelNode.addRow(rowData);
//                }
//                else
//                {
                    Object[] rowData={
                        scene.getNode(i).getID(),
                        scene.getNode(i).getAspect().getLabel()
//                        "",
//                        null,
//                        scene.getNode(i).getAspect()
                    };
                    modelNode.addRow(rowData);
//                }
                
                
                
                tableNode.setModel(modelNode);
                //tableNode.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox));
//                DefaultCellEditor dce = new DefaultCellEditor( comboBox );
//                editors.add( dce );              
    
            }
        }
    }
    
    public void updateEdges()
    {
//        editors.clear();
        comboBoxNodesId=new JComboBox();
        String dir="";        
        while(modelEdge.getRowCount()>0)
        {
            modelEdge.removeRow(0);
        }
        for(int j=0; j<scene.getSizeNodeArray();j++)
        {
            if(scene.getNode(j)!=null)
            {
                comboBoxNodesId.addItem(String.valueOf(scene.getNode(j).getID()));
            }                          
        }
        
        comboBoxNodesId.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {                    
                    Object item = event.getItem();
                    int row=-1;
                    int col=-1;
                    row=(int) editingEdgeCell.getX();
                    col=(int) editingEdgeCell.getY();
                    if(!String.valueOf(tableEdge.getModel().getValueAt(row,col)).equals((String)item))
                    {            
                        //Раскомментить, когда киря решит проблему с setFrom и setTo
                        int edgeId=(Integer)tableEdge.getModel().getValueAt(row, 0);
                        if(col==1)
                        {
                            //scene.getEdge(edgeId).setFrom(Integer.valueOf((String)item));
                        }
                        else if(col==2)
                        {
                            //scene.getEdge(edgeId).setTo(Integer.valueOf((String)item));
                        }      

                    }    
                }
            }  
        });
        
        
        
        tableEdge.setModel(modelEdge);
        for(int i=0;i<scene.getSizeEdgeArray();i++)
        {
            GraphEdge edge=scene.getEdge(i);
            if(edge!=null)
            {  
                
                if(edge.isDirectional())
                {
                    dir="True";
                }
                else
                {
                    dir="False";
                }
                Object[] rowData={
                        edge.getID(),
                        edge.getFromID(),
                        edge.getToID(),
                        dir,
                        edge.getAspect().getLabel()
                };
                
                modelEdge.addRow(rowData);
                
                }     
            
                tableEdge.setModel(modelEdge);  
                
        }
    }        
    
    	private JPopupMenu getJPopupMenu() {
            if(jButton1.isSelected())
            {
                if(tableNode.getSelectedColumnCount()==1)
                {
                    if (getJPopupMenuNodeIndividual == null)
                    {
                        getJPopupMenuNodeIndividual = new JPopupMenu();
                        getJPopupMenuNodeIndividual.add(getJMenuNodeItemDelete());
                    }
                    return getJPopupMenuNodeIndividual;
                }
                else
                {
                    if (getJPopupMenuNodeMany == null)
                    {
                        getJPopupMenuNodeMany = new JPopupMenu();
                        getJPopupMenuNodeMany.add(getJMenuNodeItemDelete());
                    }
                    return getJPopupMenuNodeMany;
                }		
            }
            else
            {
               if(tableEdge.getSelectedColumnCount()==1)
                {
                    if (getJPopupMenuEdgeIndividual == null)
                    {
                        getJPopupMenuEdgeIndividual = new JPopupMenu();
                        getJPopupMenuEdgeIndividual.add(getJMenuNodeItemDelete());
                    }
                    return getJPopupMenuEdgeIndividual;
                }
                else
                {
                    if (getJPopupMenuEdgeMany == null)
                    {
                        getJPopupMenuEdgeMany = new JPopupMenu();
                        getJPopupMenuEdgeMany.add(getJMenuNodeItemDelete());
                    }
                    return getJPopupMenuEdgeMany;
                } 
            }
        }

        private JMenuItem getJMenuNodeItemDelete() {
            if (jMenuItemDelete == null) {
                jMenuItemDelete = new JMenuItem("Удалить");
            }
            jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int selectedRowsCount=tableNode.getSelectedRowCount();
                    if(selectedRowsCount==0)
                    {
                        return;
                    }
                    else
                    {
                        int selectedRows[]=tableNode.getSelectedRows();  
                        int nodeID;
                        for(int i=0; i<selectedRowsCount; i++)
                        {
                            nodeID=(int) tableNode.getModel().getValueAt(selectedRows[i], 0);
                            scene.removeNode(nodeID);                            
                        }
                        updateScene();
                    }               
                }
            });            
            return jMenuItemDelete;
	}
    
      /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setMaximumSize(new java.awt.Dimension(9999, 9999));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton1.setText("Узлы");
        jButton1.setFocusable(false);
        jButton1.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Рёбра");
        jButton2.setFocusable(false);
        jButton2.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Конфигурация");
        jButton3.setFocusable(false);
        jButton3.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Добавить узел");
        jButton4.setFocusable(false);
        jButton4.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Добавить ребро");
        jButton5.setFocusable(false);
        jButton5.setMinimumSize(new java.awt.Dimension(20, 20));

        jButton8.setText("Поиск/Замена");
        jButton8.setFocusable(false);
        jButton8.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 13, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                        .addGap(83, 83, 83)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jScrollPane1.setViewportView(tableNode);        
        jButton1.setSelected(true);
        jButton2.setSelected(false);
        updateNodes();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jScrollPane1.setViewportView(tableEdge);
        jButton2.setSelected(true);
        jButton1.setSelected(false);
        updateEdges();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        propertiesFrame.setLocation(
                mainFrame.getLocationOnScreen().x+
                mainFrame.getWidth()/2-
                propertiesFrame.getWidth()/2,
                mainFrame.getLocationOnScreen().y+
                mainFrame.getHeight()/2-
                propertiesFrame.getHeight()/2 
                );  
        propertiesFrame.setVisible(true);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
         
        nodeCreate.setLocation(
                mainFrame.getLocationOnScreen().x+
                mainFrame.getWidth()/2-
                nodeCreate.getWidth()/2,
                mainFrame.getLocationOnScreen().y+
                mainFrame.getHeight()/2-
                nodeCreate.getHeight()/2 
                ); 
        nodeCreate.setVisible(true);
        if(nodeCreate.getNice())
        {
            String s = nodeCreate.getMyText();
            scene.createNode(NodeAspect.eNodeAspectType.BOX, NodeAspect.eNodeAspectType.TEXT);
            scene.getNode(scene.getCountNodes()-1).getAspect().createLabel(s);
            updateNodes();
        }
        nodeCreate.clearData();
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if(textDialog.isVisible())
        {
            textDialog.setVisible(false);
        }
        else
        {
            textDialog.setVisible(true);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
