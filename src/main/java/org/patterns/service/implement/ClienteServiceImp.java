package org.patterns.service.implement;

import jakarta.persistence.EntityNotFoundException;
import org.patterns.model.Cliente;
import org.patterns.model.Endereco;
import org.patterns.repository.ClienteRepository;
import org.patterns.repository.EnderecoRepository;
import org.patterns.service.ClienteService;
import org.patterns.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class ClienteServiceImp implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {

        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElseThrow(() -> new EntityNotFoundException("Não consta no banco de dados."));
        //return (Cliente) clienteRepository.findAllById(Collections.singleton(id));
    }


    @Override
    public void inserir(Cliente cliente) {

        String cep = cliente.getEndereco().getCep();

        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });

        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);


    }

    @Override
    public void atualizar(Long id, Cliente cliente) {

        Optional<Cliente> clienteDB = clienteRepository.findById(id);
        if (clienteDB.isPresent()){
            inserir(cliente);
        }


    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }
}
