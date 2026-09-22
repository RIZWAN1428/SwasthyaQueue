    package com.swasthyaqueue.backend.security;

    import io.jsonwebtoken.Jwts;

    import io.jsonwebtoken.security.Keys;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    import java.security.Key;
    import java.util.Date;

    @Component //same idea as a service ,marks this as spring managed bean, injectable elsewhere.
    public class JwtUtil {
        
        //Generate one secure key
        //private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        //method for read secret key from application properties
        @Value("${jwt.secret}")
        private String secretString;
        private Key getSigningKey(){
            return Keys.hmacShaKeyFor(secretString.getBytes());
        }
        private final long expirationMillis = 3600000; //1 hour
        //Generate token
        public String generateToken(String userName, String role){
            return Jwts.builder()
                    .setSubject(userName)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                    .signWith(getSigningKey())
                    .compact();
        }

        //Validate Token
        public boolean validateToken(String token){
            try{
                Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
                return true;
            }catch(Exception e){
                return false;
            }
        }

        //Get User Name from token
        public String getUserNameFromToken(String token){
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                        .getBody().getSubject();
        }

        //Get Role from Token
        public String getRoleFromToken(String token){
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                        .getBody().get("role", String.class);
        }
    }
