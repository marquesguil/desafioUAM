package com.linx.desafio.resources;

import com.linx.desafio.DTO.ClienteDTO;
import com.linx.desafio.domain.Cliente;
import com.linx.desafio.exception.ObjectNotFoundException;
import com.linx.desafio.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/cliente")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable Integer id) throws ObjectNotFoundException {
        Cliente obj = service.buscar(id);
        return ResponseEntity.ok().body(obj);
    }

    /*
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody Cliente obj){
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }*/

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Validated @RequestBody ClienteDTO objDto, @PathVariable Integer id) {
        Cliente obj = service.fromDTO(objDto);
        obj.setId(id);
        obj = service.update(obj);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ClienteDTO>> findAll() throws ObjectNotFoundException {
        List<Cliente> list = service.findAll();
        List<ClienteDTO> listDto = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());//ClienteDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @RequestMapping(value = "/value", method = RequestMethod.GET)
    public ResponseEntity<Page<ClienteDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0")Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24")Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
        Page<ClienteDTO> listDto = list.map(obj -> new ClienteDTO(obj));
        return ResponseEntity.ok().body(listDto);
    }
}
