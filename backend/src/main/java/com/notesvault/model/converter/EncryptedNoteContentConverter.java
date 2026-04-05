package com.notesvault.model.converter;

import com.notesvault.crypto.NoteContentCipher;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = false)
public class EncryptedNoteContentConverter implements AttributeConverter<String, String> {

    private final NoteContentCipher cipher;

    public EncryptedNoteContentConverter(NoteContentCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return cipher.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return cipher.decrypt(dbData);
    }
}
