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
@Table(name = "usuarios")
public class UsuarioVO {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idusuario;

    @Column(unique = true)
    private String dni;
    
    private String nombre;
    
    private String email;
    
	private int telefono;

//    private String contrasena;

    private String contrasenacifrada;

    private boolean activado;

//    @ManyToMany( mappedBy = "usuarios", fetch = FetchType.EAGER)
    @ManyToMany(cascade = {CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuariorol",
        joinColumns = { @JoinColumn(name = "idusuario") },
        inverseJoinColumns = { @JoinColumn(name = "idrol") }
    )
    @JsonIgnore
    private Set<RolVO> roles;

    public UsuarioVO(String nombre, String contrasenacifrada, boolean activado) {
        this.nombre = nombre;
        this.contrasenacifrada = contrasenacifrada;
        this.activado = activado;
        this.roles = new HashSet<>();
    }

    public UsuarioVO(int idusuario, String nombre, String contrasenacifrada, boolean activado, Set<RolVO> roles) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.contrasenacifrada = contrasenacifrada;
        this.activado = activado;
        this.roles = roles;
    }

    public UsuarioVO() {
        this.roles = new HashSet<>();
    }
    
 

    @Override
    public int hashCode() {
        return Objects.hash(idusuario);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioVO usuario = (UsuarioVO) o;
        return idusuario == usuario.idusuario;
    }

	@Override
	public String toString() {
		return "Usuario [idusuario=" + idusuario + ", nombre=" + nombre 
				+ ", contrasenacifrada=" + contrasenacifrada + ", activado=" + activado + "]";
	}
    

	
}
