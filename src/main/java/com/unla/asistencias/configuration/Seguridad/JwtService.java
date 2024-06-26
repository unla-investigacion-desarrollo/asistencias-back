package com.unla.asistencias.configuration.Seguridad;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service("jwtService")
public class JwtService {
	
	private static final String Propietario = "UNLA";
	private static final String Secreto = "MyClaveSecreta";
	private static final String Bearer = "Bearer ";
	
	public String createToken(String user, List<String> roles){
		return JWT.create()
				.withIssuer(JwtService.Propietario)
				.withIssuedAt(new Date())
				.withNotBefore(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 36000000))
				.withClaim("user", user)
				.withArrayClaim("roles", roles.toArray(new String[0]))
				.sign(Algorithm.HMAC256(JwtService.Secreto));
	}
	
	public boolean isBearer(String autorizacion) {
		return autorizacion != null && autorizacion.startsWith(Bearer) && autorizacion.split("\\.").length == 3;
	}
	
	private DecodedJWT verify(String autorizacion) throws Exception
	{
		if (!this.isBearer(autorizacion)) {
			throw new Exception("no esta el Bearer");
		}
		try {
			return JWT.require(Algorithm.HMAC256(Secreto))
					.withIssuer(Propietario).build()
					.verify(autorizacion.substring(Bearer.length()));
		}catch (Exception e) {
			throw new Exception ("JWT Erroneo: " + e.getMessage());
		}
	}
	
	public String getUser(String autorizacion)
	{
		try {
			return this.verify(autorizacion).getClaim("user").asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getRoles(String autorizacion)
	{
		try {
			return Arrays.asList(this.verify(autorizacion).getClaim("roles").asArray(String.class));
		} catch (JWTDecodeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
