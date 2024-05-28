package com.unla.RestApi.models.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Usuario {
    
	@Id
    @Column(unique = true, nullable = true, length = 50)
    private String email;
	
    @Column(nullable = true, length = 20)
    private String nombre;
    
    @Column(nullable = true, length = 20)
    private String apellido;
    
    @Column(nullable = true, length = 60)
    private String password;
    
    @Column(nullable = true, length = 10)
    private String rol;
}
