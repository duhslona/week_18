package my.backend.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleDto implements GrantedAuthority {

    private String name;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name;
    }
}
