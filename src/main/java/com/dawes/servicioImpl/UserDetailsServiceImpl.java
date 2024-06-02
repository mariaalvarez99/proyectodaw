package com.dawes.servicioImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.repositorio.UsuarioRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
    private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {

		Optional<UsuarioVO> usuario = usuarioRepository.findByDni(dni);
		
		if (!usuario.isPresent()) {
			System.out.println("Usuario no encontrado: " + dni);
            throw new UsernameNotFoundException("Usuario " + dni + " no existe en la base de datos");
		}
		System.out.println("Usuario encontrado: " + usuario);
		
		List<GrantedAuthority> grantList = new ArrayList<>();
        if (usuario.get().getRoles() != null) {
            for (RolVO role : usuario.get().getRoles()) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getNombre());
                grantList.add(authority);
            }
        }

        UserDetails userDetails = new User(usuario.get().getDni(), usuario.get().getContrasenacifrada(), grantList);

        return userDetails;
		
	}
	
}
