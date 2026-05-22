terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}

provider "google" {
  project = var.project_id
  region  = var.region
  zone    = var.zone
}

resource "google_project_service" "compute" {
  service            = "compute.googleapis.com"
  disable_on_destroy = false
}

resource "google_compute_instance" "app" {
  name         = "selenium-demo"
  machine_type = "e2-micro"
  zone         = var.zone

  boot_disk {
    initialize_params {
      image = "ubuntu-os-cloud/ubuntu-2204-lts"
      size  = 20
    }
  }

  network_interface {
    network = "default"
    access_config {}
  }

  metadata = {
    ssh-keys  = "ubuntu:${file("~/.ssh/kaz_to_git.pub")}"
    user-data = templatefile("user_data.sh", {
      repo_url = var.repo_url
    })
  }

  tags = ["selenium-demo"]

  depends_on = [google_project_service.compute]
}
