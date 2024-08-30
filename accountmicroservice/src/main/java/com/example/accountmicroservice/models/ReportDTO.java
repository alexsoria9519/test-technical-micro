package com.example.accountmicroservice.models;

import lombok.Data;

@Data
public class ReportDTO {
    private String fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipo;
    private Double saldoInicial;
    private Boolean estado;
    private Double movimiento;
    private Double saldoDisponible;
}
