package paquete.modelo;

import paquete.modelo.ConexionBD;
import paquete.modelo.Usuario;


import java.awt.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
/**
 * Copyright (c) 2025 Juan Diego Millán Arango
 *
 * Todos los derechos reservados.
 *
 * Este código fuente es propiedad de Juan Diego Millán Arango.
 * Su uso, copia, distribución o modificación no está permitida sin autorización explícita por escrito.
 */


/**
 * Clase que maneja las operaciones de base de datos relacionadas con los clientes.
 * Permite agregar, eliminar, actualizar y obtener clientes desde la base de datos.
 */
public class ModeloSpartanos {

    private final paquete.modelo.ConexionBD ConexionBD = new ConexionBD();


    public void agregar(Usuario usuario) {
        Connection con = ConexionBD.getConnection();
        String query = "INSERT INTO spartanos (nombre, aria, fecha_vencimiento, alerta, dias_pagados) VALUES (?, ?, ?, ?, ?)";

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 1. Parsear la fecha aria
            LocalDate fechaAria = LocalDate.parse(usuario.getAria(), formatter);

            // 2. Calcular fecha de vencimiento
            LocalDate fechaVencimiento = fechaAria.plusDays(usuario.getDias_pagados());
            String fechaVencimientoStr = fechaVencimiento.format(formatter);

            // 3. Calcular días entre hoy y vencimiento
            LocalDate hoy = LocalDate.now();
            long diasDiferencia = ChronoUnit.DAYS.between(hoy, fechaVencimiento);

            // 4. Construir mensaje de alerta
            String alerta;
            if (diasDiferencia < 0) {
                alerta = "VENCIDO (hace " + Math.abs(diasDiferencia) + " días)";
            } else {
                alerta = "Faltan " + diasDiferencia + " días";
            }

            // 5. Insertar en BD
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getAria());
            pst.setString(3, fechaVencimientoStr);
            pst.setString(4, alerta);
            pst.setInt(5, usuario.getDias_pagados());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Spartano agregado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar Spartano.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al calcular la fecha de vencimiento o alerta: " + e.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Elimina un cliente de la base de datos por su ID.
     *
     * @param id El ID del cliente a eliminar.
     */
    public void eliminar(int id) {
        Connection con = ConexionBD.getConnection();
        String query = "DELETE FROM spartanos WHERE id = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Spartano eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el Spartano.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void actualizar(Usuario usuario) {
        Connection con = ConexionBD.getConnection();

        String querySelect = "SELECT nombre, aria, fecha_vencimiento, alerta, dias_pagados FROM spartanos WHERE id = ?";
        String queryUpdate = "UPDATE spartanos SET nombre = ?, aria = ?, fecha_vencimiento = ?, alerta = ?, dias_pagados = ? WHERE id = ?";

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 1. Calcular valores actualizados
            LocalDate fechaAria = LocalDate.parse(usuario.getAria(), formatter);
            LocalDate fechaVencimiento = fechaAria.plusDays(usuario.getDias_pagados());
            String fechaVencimientoStr = fechaVencimiento.format(formatter);

            LocalDate hoy = LocalDate.now();
            long diasDiferencia = ChronoUnit.DAYS.between(hoy, fechaVencimiento);
            String alerta = (diasDiferencia < 0) ? "VENCIDO (hace " + Math.abs(diasDiferencia) + " días)" : "Faltan " + diasDiferencia + " días";

            // 2. Consultar datos actuales de la BD
            PreparedStatement pstSelect = con.prepareStatement(querySelect);
            pstSelect.setInt(1, usuario.getId());
            ResultSet rs = pstSelect.executeQuery();

            if (rs.next()) {
                String nombreBD = rs.getString("nombre");
                String ariaBD = rs.getString("aria");
                String vencimientoBD = rs.getString("fecha_vencimiento");
                String alertaBD = rs.getString("alerta");
                int diasBD = rs.getInt("dias_pagados");

                // 3. Comparar si hay cambios
                boolean sinCambios =
                        nombreBD.equals(usuario.getNombre()) &&
                                ariaBD.equals(usuario.getAria()) &&
                                vencimientoBD.equals(fechaVencimientoStr) &&
                                alertaBD.equals(alerta) &&
                                diasBD == usuario.getDias_pagados();

                if (sinCambios) {
                    JOptionPane.showMessageDialog(null, "No se realizaron cambios.");
                    return;
                }
            }

            // 4. Realizar actualización
            PreparedStatement pst = con.prepareStatement(queryUpdate);
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getAria());
            pst.setString(3, fechaVencimientoStr);
            pst.setString(4, alerta);
            pst.setInt(5, usuario.getDias_pagados());
            pst.setInt(6, usuario.getId());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Spartano actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar Spartano.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al calcular fechas o comparar datos: " + e.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Obtiene todos los clientes de la base de datos, incluyendo su ID y nombre.
     *
     * @return Una lista de clientes.
     */
    public ArrayList<Usuario> obtenerTodos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps;
        ResultSet rs;

        try {
            con = ConexionBD.getConnection();
            String sql = "SELECT id, nombre FROM spartanos";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                lista.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    /**
     * Clase interna que maneja las operaciones relacionadas con el inventario de productos.
     */

}
