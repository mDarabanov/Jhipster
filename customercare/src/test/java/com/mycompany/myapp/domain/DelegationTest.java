package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class DelegationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Delegation.class);
        Delegation delegation1 = new Delegation();
        delegation1.setId(1L);
        Delegation delegation2 = new Delegation();
        delegation2.setId(delegation1.getId());
        assertThat(delegation1).isEqualTo(delegation2);
        delegation2.setId(2L);
        assertThat(delegation1).isNotEqualTo(delegation2);
        delegation1.setId(null);
        assertThat(delegation1).isNotEqualTo(delegation2);
    }
}
