package br.com.sicredi.sicrediresilience4j.repository;

import br.com.sicredi.sicrediresilience4j.dto.GitRepositoryDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GitRepositoryMongo extends MongoRepository<GitRepositoryDTO, String> {

    public List<GitRepositoryDTO> findByUsername(final String username);
}
