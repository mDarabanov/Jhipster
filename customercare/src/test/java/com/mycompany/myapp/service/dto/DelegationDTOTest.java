package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class DelegationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DelegationDTO.class);
        DelegationDTO delegationDTO1 = new DelegationDTO();
        delegationDTO1.setId(1L);
        DelegationDTO delegationDTO2 = new DelegationDTO();
        assertThat(delegationDTO1).isNotEqualTo(delegationDTO2);
        delegationDTO2.setId(delegationDTO1.getId());
        assertThat(delegationDTO1).isEqualTo(delegationDTO2);
        delegationDTO2.setId(2L);
        assertThat(delegationDTO1).isNotEqualTo(delegationDTO2);
        delegationDTO1.setId(null);
        assertThat(delegationDTO1).isNotEqualTo(delegationDTO2);
    }
}
