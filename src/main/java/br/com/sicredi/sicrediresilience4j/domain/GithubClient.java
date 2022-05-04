package br.com.sicredi.sicrediresilience4j.domain;

import br.com.sicredi.sicrediresilience4j.dto.GitRepositoryDTO;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "github", url = "${twitter.url}")
public interface GithubClient {
    @RequestLine(value = "GET ?sort=pushed")
    @GetMapping
    public List<GitRepositoryDTO> get();
}
