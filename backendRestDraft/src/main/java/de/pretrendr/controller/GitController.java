package de.pretrendr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.boot.git.GitRepositoryState;

/**
 * Manages requests concerning git related backend features.
 * 
 * @author Tristan Schneider
 *
 */
@RequestMapping("/git")
@RestController
public class GitController {
	private final GitRepositoryState gitRepositoryState;

	/**
	 * Autowired constructor.
	 * 
	 * @param gitRepositoryState
	 *            autowired {@link GitRepositoryState}
	 */
	@Autowired
	public GitController(GitRepositoryState gitRepositoryState) {
		this.gitRepositoryState = gitRepositoryState;
	}

	/**
	 * Retrieves the current state of git repo, line branch, last commit id, last
	 * author and much more.
	 * 
	 * @author Tristan Schneider
	 * @return {@link ResponseEntity} containing information about current state of
	 *         git repo
	 */
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public ResponseEntity<GitRepositoryState> version() {
		return new ResponseEntity<GitRepositoryState>(gitRepositoryState, HttpStatus.OK);
	}
}
