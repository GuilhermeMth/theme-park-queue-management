package com.themepark.model;

import java.time.LocalTime;
import java.util.Objects;

public class HorarioSessao {
    private final LocalTime horaInicio;
    private final LocalTime horaFim;

    public HorarioSessao(LocalTime horaFim, LocalTime horaInicio) {
        this.horaFim = horaFim;
        this.horaInicio = horaInicio;
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
                "horaFim=" + horaFim +
                ", horaInicio=" + horaInicio +
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
}
