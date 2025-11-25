package com.themepark.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Pattern;

public class Visitante {
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private TipoIngresso tipoIngresso;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Visitante(String cpf, LocalDate dataNascimento, String email, String nome, TipoIngresso tipoIngresso) {
        validarCpf(cpf);
        validarEmail(email);

        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.nome = nome;
        this.tipoIngresso = tipoIngresso;
    }

    public Visitante(String cpf, String dataNascimentoStr, String email, String nome, TipoIngresso tipoIngresso) {
        this(cpf, parseData(dataNascimentoStr), email, nome, tipoIngresso);
    }

    private static LocalDate parseData(String data) {
        try {
            return LocalDate.parse(data, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de nascimento inválida. Use o formato dd/MM/yyyy");
        }
    }

    private void validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }

        String cpfNumeros = cpf.replaceAll("[^0-9]", "");

        if (cpfNumeros.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpfNumeros.matches("(\\d)\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    public int calcularIdade() {
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }

    public boolean podeEntrarNaAtracao(Atracao atracao) {
        return calcularIdade() >= atracao.getFaixaEtariaMininima();
    }

    public boolean temPrioridadeParaAtracao(Atracao atracao) {
        int nivelVisitante = this.tipoIngresso.getNivelPrioridade();
        int nivelMinimo = convertePrioridadeParaNumero(atracao.getPrioridadeAceita());
        return nivelVisitante >= nivelMinimo;
    }

    private int convertePrioridadeParaNumero(NivelPrioridade nivel) {
        switch (nivel) {
            case PASSE_ELITE: return 3;
            case PASSE_PREMIUM: return 2;
            case PASSE_COMUM: return 1;
            default: return 1;
        }
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        validarCpf(cpf);
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getDataNascimentoFormatada() {
        return dataNascimento.format(FORMATTER);
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setDataNascimento(String dataNascimentoStr) {
        this.dataNascimento = parseData(dataNascimentoStr);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoIngresso getTipoIngresso() {
        return tipoIngresso;
    }

    public void setTipoIngresso(TipoIngresso tipoIngresso) {
        this.tipoIngresso = tipoIngresso;
    }

    @Override
    public String toString() {
        return "Visitante{" +
                "cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", idade=" + calcularIdade() +
                ", email='" + email + '\'' +
                ", tipoIngresso=" + tipoIngresso +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Visitante visitante = (Visitante) o;
        return Objects.equals(cpf, visitante.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cpf);
    }
}