package simo.lopez.spatify;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class MainController {

    private String urlDB = "jdbc:sqlite:db/chinook.db";
    private Connection conn;

    @FXML
    public void initialize() throws SQLException {
        conn = DriverManager.getConnection(urlDB);
    }

    public void onCerrar(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onInformeArtista(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("artistas-view.fxml"));
            Parent root = loader.load();

            ArtistasController artistasController = loader.getController();
            artistasController.cargar(conn);

            Stage stage = new Stage();
            stage.setTitle("Artistas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onInformeClientes(ActionEvent actionEvent) {
        try {
            String jasperFilePath = "informe_1.jrxml";
            InputStream inputStream = MainApplication.class.getResourceAsStream(jasperFilePath);

            System.out.println("Compilando " + jasperFilePath);

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}