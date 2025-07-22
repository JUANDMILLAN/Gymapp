package paquete.vista;
import paquete.modelo.ConexionBD;
import paquete.modelo.Usuario;
import paquete.modelo.ModeloSpartanos;
import javax.swing.table.DefaultTableCellRenderer;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Color;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VentanaSpartanosGUI extends JFrame {

    public JPanel panel1;
    public JTable table;
    public JTextField textField1;
    public JTextField textField2;
    public JTextField textField3;
    public JTextField textField4;
    public JTextField textField5;
    public JTextField textField6;
    public JButton agregarButton;
    public JButton editarButton;
    public JButton eliminarButton;
    private JLabel labelspartanos;
    private JLabel vencidos;
    private JLabel regla;
    private JTextField Buscaritem;
    private JButton importarExcelButton;
    private JButton borradorcolumn;
    private JButton exportarExcelButton;


    int filas = 0;

    ModeloSpartanos modeloSpartanos = new ModeloSpartanos();
    /**
     * Copyright (c) 2025 Juan Diego Millán Arango
     *
     * Todos los derechos reservados.
     *
     * Este código fuente es propiedad de Juan Diego Millán Arango.
     * Su uso, copia, distribución o modificación no está permitida sin autorización explícita por escrito.
     */


    /**
     * Constructor de la clase {@code ClientescharcoGUI}. Inicializa los componentes gráficos y configura
     * los eventos de los botones.
     */
    public VentanaSpartanosGUI() {



        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1000, 1000));
        textField1.setEnabled(false);
        textField5.setEnabled(false);
        textField4.setEnabled(false);
        mostrarDatos(); // Muestra los datos de los clientes en la tabla
        cargarDatos(); // Actualiza las tarjetas de clientes
        actualizarTarjetasClientes();

        // Configuración del botón para agregar un cliente
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textField2.getText().trim();
                String aria = textField3.getText().trim();
                String fechaVencimiento = textField4.getText().trim(); // Este se recalcula internamente, pero lo pedimos igual si lo estás usando
                String alerta = textField5.getText().trim();           // Igual, pero obligatorio si se usa
                String textoDiasPagados = textField6.getText().trim();

                // Validaciones básicas
                if (nombre.isEmpty() || aria.isEmpty() || textoDiasPagados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios: Nombre, Aria y Días Pagados.");
                    return;
                }

                try {
                    int diasPagados = Integer.parseInt(textoDiasPagados);

                    // Crear un nuevo usuario (el ID es 0 porque se autogenera en la base de datos)
                    Usuario usuario = new Usuario(0, nombre, aria, fechaVencimiento, alerta, diasPagados);
                    modeloSpartanos.agregar(usuario);

                    clear(); // Limpiar los campos de texto
                    mostrarDatos(); // Actualizar los datos en la tabla
                    cargarDatos();
                    actualizarTarjetasClientes();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El campo 'Días Pagados' debe ser un número válido.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrió un error al agregar el cliente: " + ex.getMessage());
                }
            }
        });


        // Configuración del botón para editar un cliente
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textoId = textField1.getText().trim();

                // Validar si se ha seleccionado un cliente
                if (textoId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente antes de editar.");
                    return;
                }

                try {
                    int id = Integer.parseInt(textoId);

                    // Obtener los demás campos
                    String nombre = textField2.getText().trim();
                    String aria = textField3.getText().trim();
                    String fechaVencimiento = textField4.getText().trim();  // Este se recalcula, pero lo incluimos por compatibilidad
                    String alerta = textField5.getText().trim();             // Este también
                    int diasPagados = Integer.parseInt(textField6.getText().trim());

                    // Crear el objeto Usuario y actualizarlo
                    Usuario usuario = new Usuario(id, nombre, aria, fechaVencimiento, alerta, diasPagados);
                    modeloSpartanos.actualizar(usuario);

                    clear(); // Limpiar los campos de texto
                    mostrarDatos(); // Actualizar la tabla
                    cargarDatos(); // Actualizar tarjetas
                    actualizarTarjetasClientes();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID o Días pagados no válidos. Asegúrese de que sean números enteros.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al editar el cliente: " + ex.getMessage());
                }
            }
        });


        // Configuración del botón para eliminar un cliente
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textoId = textField1.getText().trim();

                if (textoId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente antes de eliminar.");
                    return;
                }

                try {
                    int id = Integer.parseInt(textoId);

                    // Confirmación opcional
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        modeloSpartanos.eliminar(id);
                        clear(); // Limpiar los campos de texto
                        mostrarDatos(); // Actualizar los datos en la tabla
                        cargarDatos(); // Actualizar las tarjetas de clientes
                        actualizarTarjetasClientes();
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID no válido. Seleccione un cliente válido.");
                }
            }
        });

        borradorcolumn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField2.setText("");
                textField3.setText("");
                textField4.setText("");
                textField5.setText("");
                textField6.setText("");
                textField1.setText("");
            }
        });


        // Evento para seleccionar un cliente de la tabla
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selecfila = table.getSelectedRow();
                if (selecfila >= 0) {
                    // Llenar los campos de texto con los datos del cliente seleccionado
                    textField1.setText(table.getValueAt(selecfila, 0).toString());
                    textField2.setText((String) table.getValueAt(selecfila, 1));
                    textField3.setText((String) table.getValueAt(selecfila, 2));
                    textField4.setText((String) table.getValueAt(selecfila, 3));
                    textField5.setText((String) table.getValueAt(selecfila, 4));
                    textField6.setText(table.getValueAt(selecfila, 5).toString());
                    filas = selecfila;
                }
            }
        });
        Buscaritem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarTabla(Buscaritem.getText()); // Filtrar la tabla cada vez que se escribe en el campo
            }
        });

        // Dentro de public VentanaSpartanosGUI() { ... }

        importarExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importarDesdeExcel(); // Llama al método que ya escribiste
            }
        });
        exportarExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarATablaExcel();
            }
        });

    }

    /**
     * Limpia los campos de texto del formulario.
     */
    public void clear() {
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField1.setText(""); // Limpia el campo de ID
    }




    /**
     * Muestra los datos de los clientes en la tabla {@code table1}.
     */
    public void mostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Fecha de pago");
        modelo.addColumn("Fecha de vencimiento");
        modelo.addColumn("Alerta");
        modelo.addColumn("Días pagados");

        table.setModel(modelo);

        // Conexión a la base de datos para obtener los datos de los clientes
        Connection con = new ConexionBD().getConnection();
        try {
            Statement st = con.createStatement();
            String query = "SELECT * FROM spartanos";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("aria");
                fila[3] = rs.getString("fecha_vencimiento");
                fila[4] = rs.getString("alerta");
                fila[5] = rs.getString("dias_pagados");
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al mostrar los datos: " + e.getMessage());
        }
    }
    public void exportarATablaExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");
        fileChooser.setSelectedFile(new File("spartanos_export.xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Spartanos");

                // Crear encabezados
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                }

                // Agregar filas de datos
                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object valor = table.getValueAt(i, j);
                        if (valor != null) {
                            cell.setCellValue(valor.toString());
                        }
                    }
                }

                // Escribir el archivo
                try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                    workbook.write(fos);
                }

                JOptionPane.showMessageDialog(this, "Exportación exitosa a: " + fileToSave.getAbsolutePath());

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
            }
        }
    }

    public void importarDesdeExcel() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoExcel = fileChooser.getSelectedFile();

            try (FileInputStream fis = new FileInputStream(archivoExcel);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet hoja = workbook.getSheetAt(0);
                for (Row fila : hoja) {
                    if (fila.getRowNum() == 0) continue; // Saltar encabezados

                    String nombre = fila.getCell(0).getStringCellValue();
                    Cell cellFecha = fila.getCell(1);
                    Date fechaAria;
                    if (cellFecha.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellFecha)) {
                        fechaAria = cellFecha.getDateCellValue();
                    } else {
                        // Si no es una fecha válida, omitir esta fila
                        continue;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String aria = sdf.format(fechaAria); // Convertimos la fecha a texto como "yyyy-MM-dd"

                    int diasPagados = (int) fila.getCell(2).getNumericCellValue();

                    // Calcular fecha vencimiento
                    java.util.Date hoy = new java.util.Date();
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(hoy);
                    cal.add(java.util.Calendar.DAY_OF_YEAR, diasPagados);
                    java.util.Date fechaVencimiento = cal.getTime();

                    SimpleDateFormat sdfVenc = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaVencStr = sdfVenc.format(fechaVencimiento);

                    // Calcular alerta
                    long diff = (fechaVencimiento.getTime() - hoy.getTime()) / (1000 * 60 * 60 * 24);
                    String alerta = (diff <= 0) ? "VENCIDO" : "Faltan " + diff + " días";

                    Usuario usuario = new Usuario(0, nombre, aria, fechaVencStr, alerta, diasPagados);
                    modeloSpartanos.agregar(usuario);
                }

                JOptionPane.showMessageDialog(this, "Importación completada correctamente.");
                mostrarDatos();
                cargarDatos();
                actualizarTarjetasClientes();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al importar: " + ex.getMessage());
            }
        }
    }
    public void filtrarTabla(String busqueda) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        table.setRowSorter(sorter);

        // Filtro por el texto ingresado (ID o cualquier otra columna)
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + busqueda));
    }
    public void cargarDatos() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setRowCount(0); // Limpiar tabla

        Connection con = new ConexionBD().getConnection();
        try {
            Statement st = con.createStatement();
            String query = "SELECT * FROM spartanos";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("aria");
                fila[3] = rs.getString("fecha_vencimiento");
                fila[4] = rs.getString("alerta");
                fila[5] = rs.getString("dias_pagados");
                modelo.addRow(fila);
            }

            // ⬇️ AQUI PEGAS EL RENDERER
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {

                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setForeground(Color.BLACK);

                    // Solo aplicar colores si no está seleccionada (para no interferir con selección)
                    if (!isSelected) {
                        String alerta = table.getValueAt(row, 4).toString(); // Columna alerta

                        if (alerta.contains("VENCIDO")) {
                            c.setBackground(Color.RED);
                        } else if (alerta.contains("Faltan")) {
                            try {
                                int dias = Integer.parseInt(alerta.split(" ")[1]);
                                if (dias <= 5) {
                                    c.setBackground(Color.YELLOW);
                                } else {
                                    c.setBackground(Color.WHITE);
                                }
                            } catch (NumberFormatException e) {
                                c.setBackground(Color.WHITE);
                            }
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                    }

                    return c;
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al mostrar los datos: " + e.getMessage());
        }
    }
    public int contarSpartanosVencidos() {
        int totalVencidos = 0;
        String sql = "SELECT COUNT(*) FROM spartanos WHERE alerta LIKE '%VENCIDO%'";

        try (Connection con = new ConexionBD().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                totalVencidos = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalVencidos;
    }
    public int contarSpartanosEnRegla() {
        int totalEnRegla = 0;
        String sql = "SELECT COUNT(*) FROM spartanos WHERE alerta NOT LIKE '%VENCIDO%'";

        try (Connection con = new ConexionBD().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                totalEnRegla = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalEnRegla;
    }


    public void actualizarTarjetasClientes() {
        int totalSpartanos = table.getRowCount();
        labelspartanos.setText(String.valueOf(totalSpartanos));

        int svencidos = contarSpartanosVencidos();
        vencidos.setText(String.valueOf(svencidos)); // Asegúrate de tener ese JLabel en tu GUI

        int sregla = contarSpartanosEnRegla();
        regla.setText(String.valueOf(sregla)); // Asegúrate de tener ese JLabel en tu GUI




    }





    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        VentanaSpartanosGUI ventana = new VentanaSpartanosGUI();
        ventana.setContentPane(ventana.panel1);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.pack();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
    }
}
