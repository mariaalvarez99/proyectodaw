package com.dawes.modelo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "roles")
public class RolVO {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idrol;

    @Column(unique = true)
    private String nombre;

//    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "usuariorol",
//        joinColumns = { @JoinColumn(name = "idrol") },
//        inverseJoinColumns = { @JoinColumn(name = "idusuario") }
//    )
//    @JsonIgnore
    @ManyToMany( mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<UsuarioVO> usuarios;

    public RolVO(String nombre) {
        this.nombre = nombre;
        this.usuarios = new HashSet<>();
    }

    public RolVO(int idrol, String nombre, Set<UsuarioVO> usuarios) {
        this.idrol = idrol;
        this.nombre = nombre;
        this.usuarios = usuarios;
    }

    public RolVO() {
        this.usuarios = new HashSet<>();
    }

    @Override
    public int hashCode() {
        return Objects.hash(idrol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolVO rol = (RolVO) o;
        return idrol == rol.idrol;
    }

	@Override
	public String toString() {
		return "Rol [idrol=" + idrol + ", nombre=" + nombre + "]";
	}

	
	
}
