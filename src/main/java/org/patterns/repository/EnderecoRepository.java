package org.patterns.repository;

import org.patterns.model.Endereco;
import org.springframework.data.repository.CrudRepository;


public interface EnderecoRepository extends CrudRepository<Endereco, String> {

}
