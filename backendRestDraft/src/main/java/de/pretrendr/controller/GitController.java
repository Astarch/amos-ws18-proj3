package de.pretrendr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.boot.git.GitRepositoryState;

@RequestMapping("/git")
@RestController
public class GitController {
	private final GitRepositoryState gitRepositoryState;

	public GitController(GitRepositoryState gitRepositoryState) {
		this.gitRepositoryState = gitRepositoryState;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 * @author Tristan Schneider
	 */
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public ResponseEntity<GitRepositoryState> version() {
		return new ResponseEntity<GitRepositoryState>(gitRepositoryState, HttpStatus.OK);
	}
}
