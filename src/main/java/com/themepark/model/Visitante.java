package com.themepark.model;

import java.util.Objects;

public class Visitante {
    private String nome;
    private String cpf;
    private String dataNascimento;
    private String email;
    private TipoIngresso tipoIngresso;

    public Visitante(String cpf, String dataNascimento, String email, String nome, TipoIngresso tipoIngresso) {
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.nome = nome;
        this.tipoIngresso = tipoIngresso;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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
                ", dataNascimento='" + dataNascimento + '\'' +
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
