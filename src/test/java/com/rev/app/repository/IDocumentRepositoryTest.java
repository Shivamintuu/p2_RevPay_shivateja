package com.rev.app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rev.app.entity.Document;
import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

@ExtendWith(MockitoExtension.class)
class IDocumentRepositoryTest {

    @Mock
    private IDocumentRepository documentRepository;

    @Test
    void findByUserId_ReturnsDocuments() {
        User user = new User();
        user.setId(1L);
        user.setEmail("doc@test.com");
        user.setFullName("Doc User");
        user.setPassword("pass");
        user.setRole(Role.PERSONAL);

        Document doc = new Document();
        doc.setUser(user);
        doc.setFileName("file.txt");
        doc.setFileType("text/plain");
        doc.setDocumentType("ID");
        doc.setData(new byte[]{1, 2, 3});
        doc.setVerified(false);

        when(documentRepository.findByUserId(1L)).thenReturn(Collections.singletonList(doc));

        List<Document> found = documentRepository.findByUserId(1L);

        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }
}
