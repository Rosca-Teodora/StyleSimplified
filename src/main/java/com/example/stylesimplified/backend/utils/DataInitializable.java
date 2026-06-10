package com.example.stylesimplified.backend.utils;

// interfata facuta special pentru a putea trimite obiecte catre alte scene din aplicatie (gen pentru details mode sau edit mode)
// e implementata de controllerele care vor sa utilizeze date trimise de alte controllere
// necesar pt callback
public interface DataInitializable<T> {
    void initData(T data);
}
