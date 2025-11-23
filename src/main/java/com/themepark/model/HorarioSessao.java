package com.themepark.model;

import java.time.LocalTime;
import java.util.Objects;

public class HorarioSessao {
    private final LocalTime horaInicio;
    private final LocalTime horaFim;

    // CORRIGIDO: Ordem dos parâmetros agora está correta
    public HorarioSessao(LocalTime horaInicio, LocalTime horaFim) {
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    @Override
    public String toString() {
        return "HorarioSessao{" +
                "horaInicio=" + horaInicio +
                ", horaFim=" + horaFim +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HorarioSessao that = (HorarioSessao) o;
        return Objects.equals(horaInicio, that.horaInicio) && Objects.equals(horaFim, that.horaFim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(horaInicio, horaFim);
    }

    public int calcularDuracaoMinutos() {
        int totalMinutosInicio = this.horaInicio.getHour() * 60 + this.horaInicio.getMinute();
        int totalMinutosFim = this.horaFim.getHour() * 60 + this.horaFim.getMinute();
        int duracaoMinutos = totalMinutosFim - totalMinutosInicio;

        if (duracaoMinutos < 0) {
            duracaoMinutos += 1440;
        }

        return duracaoMinutos;
    }
}