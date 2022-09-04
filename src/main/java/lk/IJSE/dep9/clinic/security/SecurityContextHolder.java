package lk.IJSE.dep9.clinic.security;

public class SecurityContextHolder {

    private static User user;

    public static  User getPrinciple(){
        if (user==null){
            throw new RuntimeException("No authenticated user");
        }
        return user;
    }
    public static void setPrinciple(User user){
        if (user== null){
            throw new NullPointerException("Principle Can't be null");
        }else if (user.getUsername()== null||
        !user.getUsername().isBlank()|| user.getRole()==null){
            throw new RuntimeException("Invalid user");
        }
        SecurityContextHolder.user = user;

    }

    public void clear(){ user= null; }
}
